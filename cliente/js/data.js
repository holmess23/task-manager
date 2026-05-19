
//const API_URL = 'http://localhost:8080/api/tasks';
//const CAT_URL = 'http://localhost:8080/api/categories';


const API_URL = '/api/tasks';
const CAT_URL = '/api/categories';

let filterMode = 'TODAS';
let searchText = '';
let tasks = [];           
let taskCount = 0;
let categories = [];

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

async function loadTasks() {
    try {
        const params = new URLSearchParams();

        if (filterMode === 'COMPLETAS') params.append('completed', 'true');
        if (filterMode === 'INCOMPLETAS') params.append('completed', 'false');
        if (searchText && searchText.trim() !== '') {
            params.append('search', searchText.trim());
        }

        const queryString = params.toString();
        const url = queryString ? `${API_URL}?${queryString}` : API_URL;
        console.log('Loading tasks with URL:', url);
        const response = await fetchWithAuth(url);

        if (!response) {
            return [];
        }

        if (!response.ok) throw new Error('Error al cargar tareas');

        tasks = await response.json();
        taskCount = tasks.length;
        return tasks;

    } catch (error) {
        console.error('loadTasks:', error);
        return [];
    }
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
