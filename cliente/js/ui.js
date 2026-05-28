const progressBar = document.getElementById('progress-bar');
const progressText = document.getElementById('progress-text');
const selectsCategory = document.querySelectorAll('.categories');

function renderTasks() {
    container.innerHTML = '';
    tasks.forEach(function(task) {
        createArticle(task);
        }
    );
    updateProgressBar();
}

function createArticle(task) {
        const article = document.createElement('article');
        article.dataset.id = task.id;
        article.classList.add('task');
        let priority = task.priority.charAt(0).toUpperCase() + task.priority.slice(1);
        article.innerHTML = `
        <div class="title-container">
            <h3 class="task-title">${task.title}</h3>

            <div class="task-desc">${task.description}</div>
        </div>`;

        const categoriesContainer = document.createElement('div');
        categoriesContainer.classList.add('categories-container');
        task.categories.forEach(cat =>{
            const span = document.createElement('span');
            span.classList.add('category-badge');
            span.textContent = cat.name;
            span.style.backgroundColor = cat.color;
            categoriesContainer.appendChild(span);
        });
        article.appendChild(categoriesContainer);

        article.innerHTML += `<p>Prioridad: ${priority}</p>`
        if (task.date !== null &&task.date.trim() !== '') {
            article.innerHTML += `<p>Fecha: ${task.date}</p>`;
        }

        article.innerHTML += `<button data-action="complete">Completar</button>
        <button data-action="delete">Eliminar</button>`;
        if (task.completed) {
            article.classList.add('completed');
        }
        container.appendChild(article);

        updateCounter();

}

function updateCounter() {
    counter.textContent = `Total de tareas: ${tasks.length}`;
}

function updateProgressBar() {
    const total = tasks.length;
    const completed = tasks.filter(task => task.completed).length;

    let percentage = 0;
    if (total > 0) {
        percentage = Math.round((completed / total) * 100);
    }



    progressBar.style.background = `linear-gradient(90deg,var(--color-completada), var(--color-tarjeta)${percentage}%, var(--color-tarjeta)100%)`;
    if (percentage === 100) {
        progressBar.style.background = `var(--color-completada)`;
    }
    if (percentage === 0) {
        progressBar.style.background = `var(--color-tarjeta)`;
    }
    updateProgressText(percentage);
}

function updateProgressText(percentage) {
    progressText.textContent = `Porcentaje: ${percentage}%`;
}

function renderCategories() {
    selectsCategory.forEach(function(select) {
        select.innerHTML = '';
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = cat.name;
            option.dataset.color = cat.color;
            select.appendChild(option);
        });
    });

    const filterSelect = document.getElementById('category-filter');
    filterSelect.innerHTML = '';
    categories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat.id;
        option.textContent = cat.name;
        option.dataset.color = cat.color;
        filterSelect.appendChild(option);
    });

}

function selectedCategory(parent, name){
    const option = Array.from(parent.options).find(option => option.value === name);
    if(option){
        option.selected = true;
    }
}


function getRandomColor() {
    const colors = [
    "#1E3A8A","#065F46","#7C2D12","#5B21B6","#9D174D","#374151",
    "#0F766E","#134E4A","#78350F","#4C1D95","#831843","#1F2937",
    "#0C4A6E","#14532D","#7F1D1D","#3F6212","#1E40AF","#6B21A8",
    "#92400E","#111827"
    ];
    return colors[Math.floor(Math.random() * colors.length)];
}

async function renderAdminPanel() {
    const container = document.getElementById('admin-panel');
    if (!container) return;

    if (!isAdmin()) {
        container.style.display = 'none';
        return;
    }

    container.style.display = 'block';

    const users = await loadAllUsers();

    container.innerHTML = `
        <h2>Panel de administración</h2>
        <p class="admin-subtitle">
            ${users.length} usuario${users.length !== 1 ? 's' : ''} registrado${users.length !== 1 ? 's' : ''}
        </p>
        <div class="admin-users-list">
            ${users.map(user => `
                <div class="admin-user-card ${!user.enabled ? 'disabled' : ''} ${user.role === 'ADMIN' ? 'admin' : ''}"
                    data-user-id=${user.id}>

                    <div class="admin-user-info">
                        <strong>${user.name}</strong>
                        <span class="user-email">${user.email}</span>
                        <span class="user-meta">
                            ${user.taskCount} tarea${user.taskCount !== 1 ? 's' : ''} ·
                            Desde ${new Date(user.createdAt).toLocaleDateString('es-ES')}
                        </span>
                    </div>

                    <div class="admin-user-badges">
                        <span class="role-badge ${user.role.toLowerCase()}">
                            ${user.role}
                        </span>
                        <span class="status-badge ${user.enabled ? 'active' : 'inactive'}">
                            ${user.enabled ? 'Activo' : 'Desactivado'}
                        </span>
                    </div>

                    <div class="admin-user-actions">
                        <button
                            data-admin-action="toggle-enabled"
                            data-user-id=${user.id}
                            aria-label="${user.enabled ? 'Desactivar' : 'Activar'} usuario">
                            ${user.enabled ? 'Desactivar' : 'Activar'}
                        </button>

                        ${user.role === 'USER' ? `
                            <button
                                data-admin-action="promote"
                                data-user-id=${user.id}
                                aria-label="Promover a administrador">
                                Hacer admin
                            </button>
                        ` : `
                            <button
                                data-admin-action="demote"
                                data-user-id=${user.id}
                                aria-label="Quitar rol de administrador">
                                Quitar admin
                            </button>
                        `}
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}


function renderPagination() {
    let container = document.getElementById('pagination');
    if (!container) return;
    container.innerHTML = '';
    if (totalPages <= 1) return;

    const info = document.createElement('span');
    info.className = 'pagination-info';
    info.textContent = `Página ${filterState.page + 1} de ${totalPages}`;
    container.appendChild(info);

    const controls = document.createElement('div');
    controls.className = 'pagination-controls';

    const prevButton = document.createElement('button');
    prevButton.textContent = '<';
    prevButton.disabled = filterState.page === 0;
    prevButton.setAttribute('aria-label', 'Página anterior');
    prevButton.addEventListener('click', prevPage);
    controls.appendChild(prevButton);

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = (i + 1).toString();
        pageButton.setAttribute('aria-label', `Ir a la página ${i + 1}`);
        pageButton.addEventListener('click', () => goToPage(i));
        if(i === filterState.page) {
            pageButton.classList.add('active');
            pageButton.setAttribute('aria-current', 'page');
        }
        controls.appendChild(pageButton);
    }

    const nextButton = document.createElement('button');
    nextButton.textContent = '>';
    nextButton.disabled = filterState.page >= totalPages - 1;
    nextButton.setAttribute('aria-label', 'Página siguiente');
    nextButton.addEventListener('click', nextPage);
    controls.appendChild(nextButton);

    container.appendChild(controls);
}

function renderSortControls() {
    const container = document.getElementById('sort-controls');
    if (!container) return;

    container.innerHTML = `
        <label for="sortBy">Ordenar por:</label>
        <select id="sortBy" aria-label="Campo de ordenación">
            <option value="date">Fecha</option>
            <option value="priority">Prioridad</option>
            <option value="title">Título</option>
        </select>

        <label for="sortDir">Dirección:</label>
        <select id="sortDir" aria-label="Dirección de ordenación">
            <option value="asc">Ascendente</option>
            <option value="desc">Descendente</option>
        </select>

        <button id="applySort" type="button">Aplicar</button>
    `;

    document.getElementById('applySort').addEventListener('click', function() {
        const sortBy  = document.getElementById('sortBy').value;
        const sortDir = document.getElementById('sortDir').value;
        setSorting(sortBy, sortDir);
    });
}


function renderActiveFilters() {
    const container = document.getElementById('active-filters');
    if (!container) return;

    const active = [];

    if (filterState.completed === true)  active.push({ key: 'completed', label: 'Completadas' });
    if (filterState.completed === false) active.push({ key: 'completed', label: 'Pendientes' });
    if (filterState.search)    active.push({ key: 'search',    label: `"${filterState.search}"` });
    if (filterState.priority)  active.push({ key: 'priority',  label: filterState.priority });
    if (filterState.dueBefore) active.push({ key: 'dueBefore', label: `Antes de ${filterState.dueBefore}` });
    if (filterState.dueAfter)  active.push({ key: 'dueAfter',  label: `Después de ${filterState.dueAfter}` });
    if (filterState.overdue)   active.push({ key: 'overdue',   label: 'Vencidas' });
    if (filterState.categoryId) {
        const cat = categories.find(c => c.id == filterState.categoryId);
        if (cat) active.push({ key: 'categoryId', label: cat.name });
    }

    if (active.length === 0) {
        container.innerHTML = '';
        return;
    }

    container.innerHTML = `
        <div class="active-filters-label">Filtros activos:</div>
        <div class="filter-chips">
            ${active.map(f => `
                <span class="filter-chip">
                    ${f.label}
                    <button
                        class="chip-remove"
                        data-filter-key="${f.key}"
                        aria-label="Quitar filtro ${f.label}">
                        ×
                    </button>
                </span>
            `).join('')}
            <button id="clear-all-filters" class="clear-all">
                Limpiar todo
            </button>
        </div>
    `;


    container.querySelectorAll('.chip-remove').forEach(btn => {
        btn.addEventListener('click', async function() {
            const key = btn.dataset.filterKey;
            if (key === 'completed') filterState.completed = null;
            else filterState[key] = key === 'overdue' ? false : '';
            filterState.page = 0;

            await applyFilters();
            renderTasks();
            renderPagination();
            renderActiveFilters();
            syncFilterControls();
        });
    });


    document.getElementById('clear-all-filters')
        ?.addEventListener('click', async function() {
            resetFilters();
            await applyFilters();
            renderTasks();
            renderPagination();
            renderActiveFilters();
            syncFilterControls();
        });
}

function syncFilterControls() {
    const searchInput = document.querySelector('.titleSearch');
    if (searchInput) searchInput.value = filterState.search;

    const prioritySelect = document.getElementById('priority-filter');
    if (prioritySelect) prioritySelect.value = filterState.priority;

    const categorySelect = document.getElementById('category-filter');
    if (categorySelect) categorySelect.value = filterState.categoryId;

    const dueBefore = document.getElementById('due-before');
    if (dueBefore) dueBefore.value = filterState.dueBefore;

    const dueAfter = document.getElementById('due-after');
    if (dueAfter) dueAfter.value = filterState.dueAfter;

    const overdue = document.getElementById('overdue-filter');
    if (overdue) overdue.checked = filterState.overdue;
}