function protect() {
    window.addEventListener('configReady', (e) => {
        const config = e.detail;
        if (!config.isLoggedIn) {
            window.location.href = "login.jsp";
        }
    });
}
protect();