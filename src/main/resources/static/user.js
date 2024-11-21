document.addEventListener('DOMContentLoaded', async function () {
    await showUserEmailOnNavbar()
    await fillTableAboutUser();
});

async function dataAboutCurrentUser() {
    const response = await fetch("/api/user")
    return await response.json();
}
async function fillTableAboutUser(){
    const currentUserTable1 = document.getElementById("currentUserTable");
    const currentUser = await dataAboutCurrentUser();

    let currentUserTableHTML = "";
    currentUserTableHTML +=
        `<tr>
            <td>${currentUser.id}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.phoneNumber}</td>
            <td>${currentUser.password}</td>
            <td>${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}</td>
        </tr>`
    currentUserTable1.innerHTML = currentUserTableHTML;
}

async function showUserEmailOnNavbar() {
    const currentUserEmailNavbar = document.getElementById("currentUsernameNavbar")
    const currentUser = await dataAboutCurrentUser();
    currentUserEmailNavbar.innerHTML =
        `<strong>${currentUser.username}</strong>
                 with roles: 
                 ${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}`;
}
