document.addEventListener('DOMContentLoaded', async function () {
    await showUsernameOnNavbar()
    await fillTableAboutUser();
});

async function dataAboutCurrentUser() {
    try {
        const response = await fetch("/api/user");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('Ошибка:', error);
    }
}
async function fillTableAboutUser(){
    const currentUserTable1 = document.getElementById("currentUserTable");
    const currentUser = await dataAboutCurrentUser();
    if (currentUser) {
        let currentUserTableHTML = "";
        currentUserTableHTML +=
            `<tr>
                <td>${currentUser.id}</td>
                <td>${currentUser.username}</td>
                <td>${currentUser.phoneNumber}</td>
<!--                <td>${currentUser.password}</td>-->
                <td>${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}</td>
            </tr>`
        currentUserTable1.innerHTML = currentUserTableHTML;
    } else {
        console.error('Пользователь не найден');
    }
}

async function showUsernameOnNavbar() {
    const currentUsernameNavbar = document.getElementById("currentUsernameNavbar")
    const currentUser = await dataAboutCurrentUser();
    if (currentUser) {
        currentUsernameNavbar.innerHTML =
            `<strong>${currentUser.username}</strong>
                     with roles: 
                     ${currentUser.roles.map(role => role.roleNameWithoutRole).join(' ')}`;
    } else {
        console.error('Пользователь не найден');
    }
}
