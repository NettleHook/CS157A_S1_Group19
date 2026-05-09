function init(config) {
    document.getElementById('welcome-username').textContent = config.username;
    loadBookmarks();
    loadIngredients();
    loadRecipes();
    loadUnits();
    loadLikedRecipes();
}

window.addEventListener('configReady', (e) => init(e.detail));

document.querySelectorAll('.tab-link').forEach((link) => {
    link.addEventListener('click', function (e) {
        e.preventDefault();
        document
            .querySelectorAll('.tab-link')
            .forEach((l) => l.classList.remove('active'));
        document
            .querySelectorAll('.tab-panel')
            .forEach((p) => p.classList.remove('active'));
        this.classList.add('active');
        document.getElementById(this.dataset.tab).classList.add('active');
    });
});

async function loadBookmarks() {
    try {
        const res = await fetch('api/bookmarks');
        if (res.ok) {
            const bookmarks = await res.json();
            const container = document.getElementById('bookmarks-container');
            container.innerHTML = '';

            if (bookmarks.length === 0) {
                container.innerHTML = '<p>No bookmarks yet.</p>';
            } else {
                bookmarks.forEach((r) => {
                    const box = document.createElement('div');
                    box.className = 'bookmark-box';
                    box.style.cursor = 'pointer';

                    const name = document.createElement('span');
                    name.textContent = r.name;
                    name.onclick = () => {
                        window.location.href = `recipe_page.jsp?rsid=${r.id}`;
                    };

                    const btn = document.createElement('button');
                    btn.className = 'bookmark-btn';
                    btn.textContent = '🔖';
                    btn.onclick = async (e) => {
                        e.stopPropagation();
                        await fetch('api/bookmarks', {
                            method: 'DELETE',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ recipeId: r.id }),
                        });
                        loadBookmarks();
                    };
                    box.appendChild(name);
                    box.appendChild(btn);
                    container.appendChild(box);
                });
            }
        }
    } catch (err) {
        console.error('Failed to load bookmarks:', err);
    }
}

async function loadRecipes() {
    try {
        const res = await fetch('api/me/recipes');
        if (res.ok) {
            const json = await res.json();
            const data = json.data ?? [];
            const container = document.getElementById('recipes-container');
            container.innerHTML = '';

            if (data.length === 0) {
                container.innerHTML =
                    "<p>Recipes you've created will appear here.</p>";
            } else {
                data.forEach((r) => {
                    const box = document.createElement('button');
                    box.className = 'recipe-box';

                    box.onclick = () => {
                        window.location.href = `recipe_page.jsp?rsid=${r.id}`;
                    };

                    const name = document.createElement('span');
                    name.textContent = r.name;

                    box.appendChild(name);
                    container.appendChild(box);
                });
            }
        }
    } catch (err) {
        console.error('Failed to load ingredients:', err);
    }
}

async function loadIngredients() {
    try {
        const res = await fetch('api/me/ingredients');
        if (res.ok) {
            const json = await res.json();
            const data = json.data ?? [];
            const container = document.getElementById('ingredients-container');
            container.innerHTML = '';

            if (data.length === 0) {
                container.innerHTML =
                    '<p>Your saved ingredients will appear here.</p>';
            } else {
                data.forEach((i) => {
                    const box = document.createElement('div');
                    box.className = 'ingredient-box';

                    const name = document.createElement('span');
                    name.textContent = i.name;
                    const amount = document.createElement('span');
                    amount.textContent = i.amount;
                    const unit = document.createElement('span');
                    unit.textContent = i.unit;

                    const btn = document.createElement('button');
                    btn.className = 'ingredient-btn';
                    btn.textContent = 'X';
                    btn.onclick = ((ev) => deleteIngredient(ev, i.name))

                    box.appendChild(name);
                    box.appendChild(amount);
                    box.appendChild(unit);
                    box.appendChild(btn);
                    container.appendChild(box);
                });
            }
        }
    } catch (err) {
        console.error('Failed to load ingredients:', err);
    }
}

async function loadUnits() {
    const e = document.getElementById('ingredient-input-unit');
    e.innerHTML = '';

    try {
        const res = await fetch('api/units');
        if (res.ok) {
            const json = await res.json();
            const data = json.data ?? [];
            data.forEach((u) => {
                const option = document.createElement('option');
                option.text = u.name;
                option.value = u.id;
                e.appendChild(option);
            });
        } else {
            console.error('Failed to retrieve units:', json.error);
        }
    } catch (err) {
        console.error('Failed to retrieve units:', err);
    }
}

function showAddIngredient() {
    const e1 = document.getElementById('add-ingredient-container');
    const e2 = document.getElementById('ingredients-container');
    const show = e1.style.display == 'none';
    if (show) {
        e1.style.display = 'block';
        e2.style.display = 'none';
    } else {
        e1.style.display = 'none';
        e2.style.display = 'block';
    }
}

async function addIngredient(e) {
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);

    const error_el = document.getElementById('error');
    error_el.style.display = 'none';

    try {
        const plainData = Object.fromEntries(data.entries());

        const response = await fetch('api/me/ingredients', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(plainData),
        });

        const res = await response.json();
        if (response.status === 201) {
            form.reset();
            loadIngredients();
        } else {
            error_el.innerHTML = res.error;
            error_el.style.display = 'block';
        }
    } catch (error) {
        const error_el = document.getElementById('error');
        error_el.innerHTML = error.message;
        error_el.style.display = 'block';
    }
}

async function deleteIngredient(e, id) {
    e.preventDefault();

    try {
        const response = await fetch(`api/user/ingredients?id=${id}`, {
            method: 'DELETE',
        });

        const res = await response.json();
        if (response.ok) {
            loadIngredients();
        } else {
            error_el.innerHTML = res.error;
            error_el.style.display = 'block';
        }
    } catch (error) {
        const error_el = document.getElementById('error');
        error_el.innerHTML = error.message;
        error_el.style.display = 'block';
    }
}

async function loadLikedRecipes() {
    try {
        const res = await fetch('api/me/liked');
        if (res.ok) {
            const json = await res.json();
            const data = json.data ?? [];
            const container = document.getElementById('liked-container');
            container.innerHTML = '';

            if (data.length === 0) {
                container.innerHTML = "<p>You haven't liked any recipes yet.</p>";
            } else {
                data.forEach((r) => {
                    const box = document.createElement('button');
                    box.className = 'recipe-box';
                    box.onclick = () => {
                        window.location.href = `recipe_page.jsp?rsid=${r.id}`;
                    };
                    const name = document.createElement('span');
                    name.textContent = r.name;
                    box.appendChild(name);
                    container.appendChild(box);
                });
            }
        }
    } catch (err) {
        console.error('Failed to load liked recipes:', err);
    }
}
