async function toggleBookmark(btn, recipeId) {
    const bookmarked = btn.dataset.bookmarked === "true";
    if (bookmarked) {
        const status = await removeBookmark(recipeId);
        if (status === 200) {
            btn.dataset.bookmarked = "false";
            btn.src = "./assets/unbookmarked.svg";
        }
        else{
            errorPopup();
        }
    } else {
        const status = await addBookmark(recipeId);
        if (status === 201) {
            btn.dataset.bookmarked = "true";
            btn.src = "./assets/bookmarked.svg";
        }
        else{
            errorPopup();
        }
    }
}
// Add bookmark
async function addBookmark(recipeId) {
    const res = await fetch("api/stats/bookmark", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

// Remove bookmark
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
        }
        else{
            errorPopup();
        }
    } else {
        const status = await addLike(recipeId);
        if (status === 201) {
            btn.dataset.liked = "true";
            pic.src = "./assets/liked.svg";
            count.innerText = parseInt(count.innerText) + 1;
        }
        else{
            errorPopup();
        }
    }
}
// Add like
async function addLike(recipeId) {
    const res = await fetch("api/stats/like", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ recipeId: recipeId })
    });
    return res.status;
}

// Remove like
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
    await Promise.all([
        refreshLikeStatus(recipeId),
        refreshBookmarkStatus(recipeId),
    ]);
}

async function toggleLike() {
    const recipeId = window.recipeId;
    if (!recipeId) return;

    const res = await fetch(`api/recipe/me/liked/toggle?recipeId=${recipeId}`, {
        method: 'POST',
    });

    if (res.status === 401) {
        errorPopup();
        return;
    }

    if (res.ok) {
        const json = await res.json();
        setLikeButton(json.data.liked);
    }
}

async function refreshLikeStatus(recipeId) {
    const res = await fetch('api/recipe/me/liked');
    if (!res.ok) return;
    const json = await res.json();
    const liked = (json.data ?? []).some(r => r.id === recipeId);
    setLikeButton(liked);
}

function setLikeButton(isLiked) {
    const btn = document.getElementById('like-btn');
    if (!btn) return;
    btn.textContent = isLiked ? '♥ Liked' : '♡ Like';
    btn.classList.toggle('active', isLiked);
}

async function toggleBookmark() {
    const recipeId = window.recipeId;
    if (!recipeId) return;

    const res = await fetch(`api/recipe/me/bookmarked/toggle?recipeId=${recipeId}`, {
        method: 'POST',
    });

    if (res.status === 401) {
        errorPopup();
        return;
    }

    if (res.ok) {
        const json = await res.json();
        setBookmarkButton(json.data.bookmarked);
    }
}

async function refreshBookmarkStatus(recipeId) {
    const res = await fetch('api/recipe/me/bookmarked');
    if (!res.ok) return;
    const json = await res.json();
    const bookmarked = (json.data ?? []).some(r => r.id === recipeId);
    setBookmarkButton(bookmarked);
}

function setBookmarkButton(isBookmarked) {
    const btn = document.getElementById('bookmark-btn');
    if (!btn) return;
    btn.textContent = isBookmarked ? '🔖 Bookmarked' : '🔖 Bookmark';
    btn.classList.toggle('active', isBookmarked);
}