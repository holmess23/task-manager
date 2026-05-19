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