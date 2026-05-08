async function toggleBookmark(btn, recipeId) {
    const bookmarked = btn.dataset.bookmarked === "true";
    if (bookmarked) {
        const status = await removeBookmark(recipeId);
        if (status === 200) {
            btn.dataset.bookmarked = "false";
            btn.src = "./assets/unbookmarked.svg";
        } else {
            errorPopup();
        }
    } else {
        const status = await addBookmark(recipeId);
        if (status === 201) {
            btn.dataset.bookmarked = "true";
            btn.src = "./assets/bookmarked.svg";
        } else {
            errorPopup();
        }
    }
}

async function addBookmark(recipeId) {
    const res = await fetch("api/stats/bookmark", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

async function removeBookmark(recipeId) {
    const res = await fetch("api/stats/bookmark", {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

async function toggleLike(btn, recipeId) {
    const liked = btn.dataset.liked === "true";
    const pic = btn.querySelector('img');
    const count = btn.querySelector('span');
    if (liked) {
        const status = await removeLike(recipeId);
        if (status === 200) {
            btn.dataset.liked = "false";
            pic.src = "./assets/unliked.svg";
            count.innerText = parseInt(count.innerText) - 1;
        } else {
            errorPopup();
        }
    } else {
        const status = await addLike(recipeId);
        if (status === 201) {
            btn.dataset.liked = "true";
            pic.src = "./assets/liked.svg";
            count.innerText = parseInt(count.innerText) + 1;
        } else {
            errorPopup();
        }
    }
}

async function addLike(recipeId) {
    const res = await fetch("api/stats/like", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

async function removeLike(recipeId) {
    const res = await fetch("api/stats/like", {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

function errorPopup() {
    const popup = document.getElementById("errorPopup");
    popup.classList.add("show");
    setTimeout(() => popup.classList.remove("show"), 3000);
}

async function initStats(recipeId) {
    window.recipeId = recipeId;
}