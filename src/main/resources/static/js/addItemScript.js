document.getElementById("addForm").onsubmit = async function (e) {
    e.preventDefault();
    const input = document.getElementById("input").value;
    await fetch('/api/items', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ input: input })
    });
    window.location.href = '/';
};
