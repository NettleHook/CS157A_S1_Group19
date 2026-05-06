const DIETS = [
    { id: 'keto', text: 'Keto' },
    { id: 'atkins', text: 'Atkins' },
    { id: 'paleo', text: 'Paleo' },
    { id: 'pescatarian', text: 'Pescatarian' },
    { id: 'vegetarian', text: 'Vegetarian' },
    { id: 'vegan', text: 'Vegan' },
    { id: 'halal', text: 'Halal-friendly' },
    { id: 'kosher', text: 'Kosher-friendly' },
];

const CATEGORIES = [
    { id: 'desserts_sweets', text: 'Desserts and Sweets' },
    { id: 'bread', text: 'Bread' },
    { id: 'soups_stews', text: 'Soups and Stews' },
    { id: 'salads', text: 'Salads' },
    { id: 'dressings_sauces', text: 'Dressings and Sauces' },
    { id: 'snacks', text: 'Snacks' },
    { id: 'main_dishes', text: 'Main Dishes' },
];
let step_count = 1;
function renderCheckboxes(dataArray, containerId, inputName) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = dataArray
        .map(
            (option) => `
        <div class="checkbox-item">
            <input type="checkbox" id="${option.id}" name="${inputName}" value="${option.id}">
            <label for="${option.id}">${option.text}</label>
        </div>
    `,
        )
        .join('');
}

function renderRadioButtons(dataArray, containerId, inputName) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = dataArray
        .map(
            (option) => `
        <div class="checkbox-item">
            <input type="radio" id="${option.id}" name="${inputName}" value="${option.id}" required>
            <label for="${option.id}">${option.text}</label>
        </div>
    `,
        )
        .join('');
}

function addIngredient() {
    const container = document.getElementById('ingredients');
    const row = document.createElement('div');
    row.className = 'ingredient-row';
    row.innerHTML = `
            <input type="text" name="ingredient-input-name" placeholder="Enter ingredient name" required/>
            <input type="number" name="ingredient-input-amt" step=0.01 placeholder="Enter amount (if applicable)"/>
            <input type="text" name="ingredient-input-unit" placeholder="Enter unit" required list="units-list"/>
            <button type="button" onclick="removeIngredient(this)">Remove</button>
            `;
    container.appendChild(row);
}

function removeIngredient(btn) {
    const row = btn.parentElement;
    if (document.querySelectorAll('.ingredient-row').length > 1) {
        row.remove();
    }
}
function addStep() {
    const container = document.getElementById('steps');
    const row = document.createElement('div');
    step_count+=1;
    row.className = 'step-row';
    row.innerHTML = `
            <span>${step_count}. <span>
            <input type="text" class = "step_input" name="step"/>
            `;
    container.appendChild(row);
}

function removeStep(btn) {
    const row = btn.parentElement;
    if (document.querySelectorAll('.step-row').length > 1) {
        row.remove();
    }
}

async function uploadRecipe(e) {
    e.preventDefault();
    const form = document.getElementById('recipe-upload');
    const data = new FormData(form);

    try {
        const response = await fetch('api/upload', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(data),
        });

        const returnVal = await response.json();

        if (response.status === 200) {
            window.location.href =
                './recipe_page.jsp?rsid=' + returnVal.data.resid;
        } else {
            const error_el = document.getElementById('error');
            error_el.innerHTML = returnVal.data.error;
            error_el.style.display = 'block';
        }
    } catch (error) {
        console.error('Request failed: ', error);
        const error_el = document.getElementById('error');
        error_el.innerHTML = error.message;
        error_el.style.display = 'block';
    }
}

renderRadioButtons(CATEGORIES, 'category-container', 'food-cat');
renderCheckboxes(DIETS, 'diet-container', 'diet-cat');
