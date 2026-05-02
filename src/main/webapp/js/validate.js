async function validateUser() {
    const res = await fetch("api/validate");
    if (res.ok) {
        const json = await res.json();
        return json;
    } else {
        window.location.href = "login.jsp";
    }   
}