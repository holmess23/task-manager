
//const API_URL = 'http://localhost:8080/api/tasks';
//const CAT_URL = 'http://localhost:8080/api/categories';


const API_URL = '/api/tasks';
const CAT_URL = '/api/categories';
const ADMIN_URL = '/api/admin';

const filterState = {
    completed: null,
    search: '',
    priority: '',
    categoryId: '',
    dueBefore: '',
    dueAfter: '',
    overdue: false,
    page: 0,
    size: 10,
    sortBy: 'date',
    sortDir: 'asc'
};

let tasks = [];           
let taskCount = 0;
let categories = [];

let totalElements = 0;
let totalPages = 0;

function resetFilters(){
    Object.assign(filterState, {
    completed: null,
    search: '',
    priority: '',
    categoryId: '',
    dueBefore: '',
    dueAfter: '',
    overdue: false,
    page: 0,
    size: 10,
    sortBy: 'date',
    sortDir: 'asc'
});
}

async function applyFilters() {
    const params = new URLSearchParams();

    if(filterState.completed !== null){
        params.append('completed', filterState.completed);
    }

    if (filterState.search) params.append('search', filterState.search);
    if (filterState.priority) params.append('priority', filterState.priority);
    if (filterState.categoryId) params.append('categoryId', filterState.categoryId);
    if (filterState.dueBefore) params.append('dueBefore', filterState.dueBefore);
    if (filterState.dueAfter) params.append('dueAfter', filterState.dueAfter);
    if (filterState.overdue) params.append('overdue', true);

    params.append('page', filterState.page);
    params.append('size', filterState.size);
    params.append('sortBy', filterState.sortBy);
    params.append('sortDir', filterState.sortDir);

    const url = `${API_URL}?${params.toString()}`;

    const response = await fetchWithAuth(url);
    if (!response || !response.ok) return [];

    const pageResponse = await response.json();
    tasks = pageResponse.content;
    totalPages = pageResponse.totalPages;
    totalElements = pageResponse.totalElements;
    taskCount = tasks.length;

    return tasks;
    
}


async function loadAllUsers(){
    try {
        const response = await fetchWithAuth(`${ADMIN_URL}/users`);
        if (!response || !response.ok) {
            return [];
        }
        return await response.json();
    }catch (error) {
        console.error('Error al cargar usuarios:', error);
        return [];
    }
}

async function toggleUserEnabled(id) {
    const response = await fetchWithAuth(`${ADMIN_URL}/users/${id}/toggle-enabled`, {
        method: 'PUT'
    });
    if (!response || !response.ok) {
        throw new Error('Error al cambiar estado del usuario');
    }
    return await response.json();
    
}

async function promote(id) {
    const response = await fetchWithAuth(`${ADMIN_URL}/users/${id}/promote`, {
        method: 'PUT'
    });
    if (!response || !response.ok) {
        throw new Error('Error al promover al usuario');
    }
    return await response.json();
}

async function demote(id) {
    const response = await fetchWithAuth(`${ADMIN_URL}/users/${id}/demote`, {
        method: 'PUT'
    });
    if (!response || !response.ok) {
        throw new Error('Error al degradar al usuario');
    }   
    return await response.json();
}

async function loadCategories(){
    try {
        console.log('Loading categories with URL:', CAT_URL);
        const response = await fetchWithAuth(CAT_URL);

        if(!response) {
            return [];
        }

        if(!response.ok) throw new Error('Error al cargar categorías');
        categories = await response.json();
        return categories;
    } catch (error) {
        console.error('loadCategories: ', error);
        return [];
    }
}

async function createCategory(name, color){
    try {
        const response = await fetchWithAuth(CAT_URL, {
            method: 'POST',
            body: JSON.stringify({ name, color})
        });

        if(!response) {
            return [];
        }

        if (response.status === 401 || response.status === 403) {
            alert('Tu sesión ha expirado. Por favor, inicia sesión nuevamente.');
            logout();
            return [];
        }

        if(response.status !== 201){
            const error = await response.json();
            throw new Error(error.error || 'Error al crear categoría');
        }

        const created = await response.json();
        categories.push(created);
        return created;
    } catch (error) {
        console.error('createCategory: ', error);
        throw error;
    }
}

function goToPage(page) {
    if (page < 0 || page >= totalPages) return;
    filterState.page = page;
    applyFilters().then(() => {
        renderTasks();
        renderPagination();
    });
}

function nextPage() {
    goToPage(filterState.page + 1);
}

function prevPage() {
    goToPage(filterState.page - 1);
}

function setSorting(sortBy, sortDir) {
    currentSortBy = sortBy;
    currentSortDir = sortDir;
    filterState.page = 0; 
    applyFilters().then(() => {
        renderTasks();
        renderPagination();
    });
}

async function addTask(task) {
    try {
        console.log(JSON.stringify(task));
        const response = await fetchWithAuth(API_URL, {
            method: 'POST',
            body: JSON.stringify(task)
        });

        if (!response) { 
            return;
        }

        if (!response.ok) {
            const apiError = await response.json();
            const error = new Error(apiError.message || 'Error al crear la tarea');
            error.status = apiError.status;
            error.fieldErrors = apiError.fieldErrors;
            throw error;
        }

        const createdTask = await response.json();
        tasks.push(createdTask);
        taskCount = tasks.length;
        return createdTask;

    } catch (error) {
        console.error('Error al crear la tarea:', error);
        throw error;
    }
}

async function updateTask(id, task) {
    console.log('Updating task:', JSON.stringify(task));
    try {
        const response = await fetchWithAuth(`${API_URL}/${id}`, {
            method: 'PUT',
            body: JSON.stringify(task)
        });

        if (!response) {
            return [];
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Error al actualizar la tarea');
        }

        const taskFromServer = await response.json();
        tasks = tasks.map(t => t.id == id ? taskFromServer : t);
        return taskFromServer;

    } catch (error) {
        console.error('Error al actualizar la tarea:', error);
        throw error;
    }
}

async function toggleComplete(id) {
    const task = tasks.find(t => t.id === id);
    if (!task) return;
    return updateTask(id, { ...task, completed: !task.completed });
}

async function deleteTask(id) {
    try {
        const response = await fetchWithAuth(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response) {
            return;
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Error al eliminar la tarea');
        }

        tasks = tasks.filter(t => t.id != id);
        taskCount = tasks.length;

    } catch (error) {
        console.error('Error al eliminar la tarea:', error);
        throw error;
    }
}
