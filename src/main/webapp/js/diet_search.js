async function propogateDiets(row) {
    const diets_div = document.getElementById("diet-div");
    diets_div.innerHTML = await getDiets(row);
}

async function getDiets(row) {
    const res = await fetch("api/diets/get_diets");
    if (res.ok) {
        const response = await res.json();
        if (row == 1) {
            return buildDietCheckboxes(response.data.allDiets, response.data.userDiets);
        } else {
            return buildDietCheckboxesProfile(response.data.allDiets, response.data.userDiets)
        }
    } else if (res.status == 401) {
        //user is not logged in-- just return all the diets
        const response = await res.json();
        return buildDietCheckboxes(response.data.allDiets, []);
    } else {
        const response = await res.json();
        if (row == 1) {
            return buildDietCheckboxes(response.data.allDiets, response.data.userDiets) + "<p class='warning' style='color:red'>Could not retrieve your registered diets.</p>";
        } else {
            return buildDietCheckboxesProfile(response.data.allDiets, response.data.userDiets) + "<p class='warning' style='color:red'>Could not retrieve your registered diets.</p>";
        }
    }
}

function buildDietCheckboxes(allDiets, userDiets) {
    return allDiets.map(diet => `
            <input type="checkbox" id="${diet.id}" name="diet-cat" value="${diet.id}"
                ${userDiets.includes(diet.id) ? "checked" : ""}>
            <label for="${diet.id}">${diet.text}</label>
    `).join("");
}

function buildDietCheckboxesProfile(allDiets, userDiets) {
    return allDiets.map(diet => `
        <div class="diet-item"> 
            <input type="checkbox" id="${diet.id}" name="diet-cat" value="${diet.id}"
                ${userDiets.includes(diet.id) ? "checked" : ""}>
            <label for="${diet.id}">${diet.text}</label>
        </div>
    `).join("");
}

async function registerDiets() {
    const checked = [...document.querySelectorAll('input[name="diet-cat"]:checked')]
        .map(cb => cb.value);

    response = await fetch("api/diets/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ diets: checked })
    });
    if (!response.ok) {
        document.getElementById("diets-container").innerHTML+="<p>Failed to save your diet preferences. Please try again.</p>"
    }else{
        document.getElementById("diets-container").innerHTML+="<p>Updated successfully</p>"
        propogateDiets(0)
    }
}

async function propagateCategories() {
    const res = await fetch('api/categories');
    if (res.ok) {
        const response = await res.json();
        const div = document.getElementById('category-div');
        div.innerHTML = response.data.map(cat => `
            <input type="radio" id="${cat.id}" name="food-cat" value="${cat.id}">
            <label for="${cat.id}">${cat.text}</label>
        `).join('');
    }
}
