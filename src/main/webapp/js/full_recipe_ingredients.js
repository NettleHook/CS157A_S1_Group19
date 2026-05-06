function toggleIngredientDropdown() {
    const panel   = document.getElementById('panel');
    const chevron = document.getElementById('chevron');
    const isOpen  = panel.style.display !== 'none';

    if (isOpen) {
        panel.style.display = 'none';
        chevron.textContent = '▼';
    } else {
        panel.style.display = 'block';
        chevron.textContent = '▲';
        loadIngredients();
    }
}

function showAddIngredient(e) {
    e.stopPropagation();

    const listView = document.getElementById('list-view');
    const addView  = document.getElementById('add-view');
    const panel    = document.getElementById('panel');
    const showingAdd = addView.style.display !== 'none';

    panel.style.display = 'block';
    document.getElementById('chevron').textContent = '▲';

    if (showingAdd) {
        addView.style.display  = 'none';
        listView.style.display = 'block';
    } else {
        addView.style.display  = 'block';
        listView.style.display = 'none';
        loadUnits();
    }
}

async function loadIngredients() {
    const container = document.getElementById('ingredients-container');
    container.innerHTML = '<p>Loading…</p>';

    try {
        const res  = await fetch('api/me/ingredients');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        const json = await res.json();
        const data = json.data ?? [];
        container.innerHTML = '';

        if (data.length === 0) {
            container.innerHTML = '<p>Your saved ingredients will appear here.</p>';
            return;
        }

        data.forEach((ingredient) => {
            const box = document.createElement('div');
            box.className = 'ingredient-box';

            const name = document.createElement('span');
            name.textContent = ingredient.name;

            const btn = document.createElement('button');
            btn.className   = 'ingredient-btn';
            btn.textContent = 'X';
            btn.onclick = (ev) => deleteIngredient(ev, ingredient.name);

            box.appendChild(name);
            box.appendChild(btn);
            container.appendChild(box);
        });
    } catch (err) {
        container.innerHTML = `<p style="color:red;">Failed to load ingredients.</p>`;
        console.error('loadIngredients error:', err);
    }
}

async function loadUnits() {
    const select = document.getElementById('ingredient-input-unit');
    select.innerHTML = '';

    try {
        const res  = await fetch('api/units');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        const json = await res.json();
        const data = json.data ?? [];

        data.forEach((u) => {
            const option   = document.createElement('option');
            option.text    = u.name;
            option.value   = u.id;
            select.appendChild(option);
        });
    } catch (err) {
        console.error('loadUnits error:', err);
    }
}

async function addIngredient(e) {
    e.preventDefault();
    const form    = e.target;
    const errorEl = document.getElementById('error');
    errorEl.style.display = 'none';

    try {
        const plainData = Object.fromEntries(new FormData(form).entries());

        const response = await fetch('api/me/ingredients', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(plainData),
        });

        const res = await response.json();

        if (response.status === 201) {
            form.reset();
            document.getElementById('add-view').style.display  = 'none';
            document.getElementById('list-view').style.display = 'block';
            loadIngredients();
        } else {
            errorEl.textContent   = res.error;
            errorEl.style.display = 'block';
        }
    } catch (err) {
        errorEl.textContent   = err.message;
        errorEl.style.display = 'block';
    }
}

async function deleteIngredient(e, id) {
    e.preventDefault();
    const errorEl = document.getElementById('error');

    try {
        const response = await fetch(`api/user/ingredients?id=${id}`, {
            method: 'DELETE',
        });

        const res = await response.json();

        if (response.ok) {
            loadIngredients();
        } else {
            errorEl.textContent   = res.error;
            errorEl.style.display = 'block';
        }
    } catch (err) {
        errorEl.textContent   = err.message;
        errorEl.style.display = 'block';
    }
}