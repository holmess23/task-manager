
const forms = document.querySelectorAll('.task-form');
const container = document.getElementById('taskContainer');
const counter = document.getElementById('counter');
const inputCategory = document.getElementById('category-input');
const menuToggle = document.getElementById('menu-toggle');
const sidebar = document.getElementById('sidebar');
const searchBar = document.getElementById('searchForm');
const userNameEl = document.getElementById('user-name');
const logoutBtn = document.getElementById('logout-btn');
const maxTasks = 10;

document.addEventListener('DOMContentLoaded', async function() {
    categories = await loadCategories();
    tasks = await applyFilters();
    taskCount = tasks.length;
    renderTasks();
    renderPagination();
    renderSortControls();
    updateProgressBar();
    renderCategories();

    if(isAdmin()){
        await renderAdminPanel();
    }
});

if(userNameEl){
    userNameEl.textContent = getUserName() || '';
}

if(logoutBtn){
    logoutBtn.addEventListener('click', function() {
        if(confirm('¿Estás seguro de que deseas cerrar sesión?')) {
            logout();
        }
    });
}


forms.forEach(function(form) {
    form.addEventListener('submit', async function(event) {
        await handleTaskSubmit(event, form);
    });
});

async function handleTaskSubmit(event, form) {
    event.preventDefault();

    clearAllErrors(form);
    hideGlobalError();

    if (taskCount >= maxTasks) {
        alert('Has alcanzado el límite de tareas permitidas.');
        return;
    }

    const title = getValue(form.id, 'title');
    const priority = getValue(form.id, 'priority').toUpperCase();
    const date = getValue(form.id, 'date');
    const desc = getValue(form.id, 'description');
    const selectedCats = getValues(form.id, 'categories').map(id => Number(id));

    const task = {
        title:      title,
        description: desc,
        priority:   priority,
        date:       date || null,
        categories: selectedCats,
        completed:  false
    };

    const localErrors = validateTaskForm(task);

    if(Object.keys(localErrors).length > 0){
        Object.entries(localErrors).forEach(([field, message]) => showFieldError(`#${form.id} .${field}`, field, message));
        const firstErrorField = Object.keys(localErrors)[0];
        document.querySelector(`#${form.id} .${firstErrorField}`).focus();
        return;
    }

    try {
        await addTask(task);
        renderTasks();
        renderPagination();
        updateProgressBar();
        form.reset();
    } catch (error) {
        if(error.fieldErrors){
            showServerErrors(error.fieldErrors, form);
        } else{
            showGlobalError(error.message || "Error al crear la tarea");
        }
    }
}

function getValue(form, item) {
    const element = document.querySelector(`#${form} .${item}`);
    const value = element.value;
    return value !== undefined ? value.trim() : '';
}

function getValues(form, item) {
    const element = document.querySelector(`#${form} .${item}`);
    const options = element.selectedOptions;
    console.log(options);
    return Array.from(options).map(option => option.value);
}


container.addEventListener('click', async function(event) {
    const btn = event.target.closest('[data-action]');
    if (!btn) return;

    const id = Number(btn.closest('[data-id]').dataset.id);
    const action = btn.dataset.action;

    try {
        if (action === 'delete')   await deleteTask(id);
        if (action === 'complete') await toggleComplete(id);

        tasks = await applyFilters();
        renderTasks();
        renderPagination();
        updateProgressBar();
    } catch (error) {
        alert('Error: ' + error.message);
    }
});


document.querySelectorAll('[data-filter]').forEach(btn =>{
    btn.addEventListener('click', async function(){
        const filter = btn.dataset.filter;

        if(filter === 'COMPLETAS') filterState.completed = true;
        else if(filter === 'INCOMPLETAS') filterState.completed = false;
        else filterState.completed = null;
        
        filterState.page = 0;

        document.querySelectorAll('[data-filter]')
            .forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        await applyFilters();
        renderTasks();
        renderPagination();
        renderActiveFilters();
    })
});

let searchTimeout;
document.querySelector('.titleSearch')?.addEventListener('input', function() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(async () => {
        filterState.search = this.value.trim();
        filterState.page   = 0;
        await applyFilters();
        renderTasks();
        renderPagination();
        renderActiveFilters();
    }, 300);
});

document.getElementById('priority-filter')?.addEventListener('change', async function() {
    filterState.priority = this.value;
    filterState.page     = 0;
    await applyFilters();
    renderTasks();
    renderPagination();
    renderActiveFilters();
});

document.getElementById('category-filter')?.addEventListener('change', async function() {
    filterState.categoryId = this.value;
    filterState.page       = 0;
    await applyFilters();
    renderTasks();
    renderPagination();
    renderActiveFilters();
});

document.getElementById('due-before')?.addEventListener('change', async function() {
    filterState.dueBefore = this.value;
    filterState.page      = 0;
    await applyFilters();
    renderTasks();
    renderPagination();
    renderActiveFilters();
});

document.getElementById('due-after')?.addEventListener('change', async function() {
    filterState.dueAfter = this.value;
    filterState.page     = 0;
    await applyFilters();
    renderTasks();
    renderPagination();
    renderActiveFilters();
});

document.getElementById('overdue-filter')?.addEventListener('change', async function() {
    filterState.overdue = this.checked;
    filterState.page    = 0;
    await applyFilters();
    renderTasks();
    renderPagination();
    renderActiveFilters();
});


inputCategory.addEventListener('keydown', async function(event) {
    if (event.key === ',') {
        event.preventDefault();
        const name = inputCategory.value.trim();
        if (name === '') return;

        const exists = categories.find(c => c.name.toLowerCase() === name.toLowerCase());
        let cName = "";

        if(!exists){
            try {
                const color = getRandomColor();
                const category = await createCategory(name, color);
                cName = category.name;
                renderCategories();
            } catch (error) {
                alert('Error al crear la categoría: ' + error.message);
            }
        }
        inputCategory.value = '';
        //selectedCategory(inputCategory.parentElement, cName);
    }
});


menuToggle.addEventListener('click', function() {
    sidebar.classList.toggle('active');
    if (sidebar.classList.contains('active')) {
        enableSidebarFocus();
        sidebar.querySelector('input.title').focus();
    } else {
        disableSidebarFocus();
        menuToggle.focus();
    }
});

document.addEventListener('click', function(event) {
    if (!sidebar.contains(event.target)
        && !menuToggle.contains(event.target)
        && sidebar.classList.contains('active')) {  
        sidebar.classList.remove('active');
        disableSidebarFocus();
        
    }
    forms.forEach(form => {
        if (!form.contains(event.target)) form.reset();
    });
});

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape' && sidebar.classList.contains('active')) {
        sidebar.classList.remove('active');
        disableSidebarFocus();
        menuToggle.focus();
    }
});

function disableSidebarFocus() {
    sidebar.querySelectorAll('*').forEach(el => {
        el.setAttribute('tabindex', '-1');
    });
}

function enableSidebarFocus() {
    sidebar.querySelectorAll('input, select, button').forEach(el => {
        el.removeAttribute('tabindex');
    });
}

sidebar.addEventListener('keydown', function(event) {
    if (event.key !== 'Tab') return;

    const focusables = sidebar.querySelectorAll('input, select, button');
    const first = focusables[0];
    const last = focusables[focusables.length - 1];

    if (event.shiftKey) {
        if (document.activeElement === first) {
            event.preventDefault();
            last.focus();
        }
    } else {
        if (document.activeElement === last) {
            event.preventDefault();
            first.focus();
        }
    }
});

document.addEventListener('click', async function(event) {
    const btn = event.target.closest('[data-admin-action]');
    if (!btn) return;

    const action = btn.dataset.adminAction;
    const userId = btn.dataset.userId;

    console.log('action:', action, 'userId:', userId, 'tipo:', typeof userId);

    try {
        if (action === 'toggle-enabled') {
            await toggleUserEnabled(userId);
        }
        if (action === 'promote') {
            if (!confirm('¿Promover este usuario a administrador?')) return;
            await promote(userId);
        }
        if (action === 'demote') {
            if (!confirm('¿Quitar el rol de administrador a este usuario?')) return;
            await demote(userId);
        }

        await renderAdminPanel();

    } catch (error) {
        alert('Error: ' + error.message);
    }
});