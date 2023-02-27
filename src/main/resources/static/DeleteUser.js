$(async function () {

    deleteUser();
});

function deleteUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("http://localhost:8080/api/admin/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#deleteFormCloseButton').click();
                allUsers();
            })
    })
}