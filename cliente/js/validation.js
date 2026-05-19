function showFieldError(selector,fieldName, message){
    const field = document.querySelector(selector);
    if (!field) return;

    clearFieldError(selector, fieldName);

    field.classList.add('field-error');
    field.setAttribute('aria-invalid', 'true');
    field.setAttribute('aria-describedby', `${fieldName}-error`);

    const errorSpan = document.createElement('span');
    errorSpan.id = `${fieldName}-error`;
    errorSpan.className = 'error-message';
    errorSpan.setAttribute('role', 'alert');
    errorSpan.textContent = message;

    field.insertAdjacentElement('afterend', errorSpan);
}

function clearFieldError(selector, fieldId) {
    const field = document.querySelector(selector);
    if (!field) return;

    field.classList.remove('field-error');
    field.removeAttribute('aria-invalid');
    field.removeAttribute('aria-describedby');

    const existing = document.getElementById(`${fieldId}-error`);
    if (existing) existing.remove();
}

function clearAllErrors(form) {
    if (!form) return;

    form.querySelectorAll('.field-error').forEach(el => {
        el.classList.remove('field-error');
        el.removeAttribute('aria-invalid');
        el.removeAttribute('aria-describedby');
    });

    form.querySelectorAll('.error-message').forEach(el => el.remove());
}

function showServerErrors(fieldErrors, form) {
    if (!fieldErrors) return;
    Object.entries(fieldErrors).forEach(([field, messages]) => {
        showFieldError(`#${form.id} .${field}`,field, messages[0]);
    });
}

function showGlobalError(message) {
    let container = document.getElementById('global-error');

    if (!container) {
        container = document.createElement('div');
        container.id = 'global-error';
        container.setAttribute('role', 'alert');
        container.className = 'global-error';
        document.querySelector('form').prepend(container);
    }

    container.textContent = message;
    container.style.display = 'block';
}

function hideGlobalError() {
    const container = document.getElementById('global-error');
    if (container) container.style.display = 'none';
}

function validateTaskForm(data) {
    const errors = {};

    if (!data.title || data.title.trim().length < 3) {
        errors.title = 'El título debe tener al menos 3 caracteres';
    }

    if (data.title && data.title.length > 100) {
        errors.title = 'El título no puede superar los 100 caracteres';
    }

    if (!data.date) {
        errors.date = 'La fecha es obligatoria';
    } else {
        const selected = new Date(data.date);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        if (selected < today) {
            errors.date = 'La fecha no puede ser anterior a hoy';
        }
    }

    if (!data.priority) {
        errors.priority = 'La prioridad es obligatoria';
    }

    return errors;
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}