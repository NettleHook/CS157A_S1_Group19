async function toggleBookmark(btn, recipeId) {
    const bookmarked = btn.dataset.bookmarked === "true";
    if (bookmarked) {
        const status = await removeBookmark(recipeId);
        if (status === 200) {
            btn.dataset.bookmarked = "false";
            btn.src = "./assets/unbookmarked.svg";
        }
    } else {
        const status = await addBookmark(recipeId);
        if (status === 201) {
            btn.dataset.bookmarked = "true";
            btn.src = "./assets/bookmarked.svg";
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
    if (liked) {
        const status = await removeLike(recipeId);
        if (status === 200) {
            btn.dataset.liked = "false";
            btn.src = "./assets/unliked.svg";
        }
    } else {
        const status = await addLike(recipeId);
        if (status === 201) {
            btn.dataset.likeked = "true";
            btn.src = "./assets/liked.svg";
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