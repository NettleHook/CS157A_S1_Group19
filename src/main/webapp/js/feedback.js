async function addMessage(e){
    e.preventDefault();
    const form = document.getElementById('feedback');
    const data = new FormData(form);

    try {
        const response = await fetch('api/feedback/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(data),
        });

        const returnVal = await response.json();
            document.getElementById('feedback_form').innerHTML = "<span>Feedback has been submitted.</span>"
        if (response.status === 200) {
            
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


async function loadCategories(){
    const e = document.getElementById('feedback_category');
    e.innerHTML = '';

    try {
        const res = await fetch('api/feedback/categories');//FIXME:
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
            console.error('Failed to retrieve categories:', json.error);
        }
    } catch (err) {
        console.error('Failed to retrieve categories:', err);
    }
}
loadCategories();