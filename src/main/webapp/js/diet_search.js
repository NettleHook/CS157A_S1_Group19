async function propogateDiets() {
    const diets_div = document.getElementById("diet-items");
    diets_div.innerHTML = await getDiets();
}

async function getDiets() {
    const res = await fetch("api/diets/get_diets");
    if (res.ok) {
        const response = await res.json();
        return buildDietCheckboxes(response.data.allDiets, response.data.userDiets);
    } else if (res.status == 401) {
        //user is not logged in-- just return all the diets
        const response = await res.json();
        return buildDietCheckboxes(response.data.allDiets, []);
    } else {
        const response = await res.json();
        return buildDietCheckboxes(response.data.allDiets, [])
            + "<p class='warning' style='color:red'>Could not retrieve your registered diets.</p>";
    }
}

function buildDietCheckboxes(allDiets, userDiets) {
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
        alert("Failed to save your diet preferences. Please try again.");
    }
}
propogateDiets();