async function createNewUser(user) {
    await fetch("/api/admin",
        {method: 'POST',headers: {'Content-Type': 'application/json'}, body: JSON.stringify(user)})
    console.log(JSON.stringify(user) + "hello");
}

async function addNewUserForm() {
    const newUserForm = document.getElementById("newUser");

    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const username = newUserForm.querySelector("#username").value.trim();
        const phoneNumber = newUserForm.querySelector("#phoneNumber").value.trim();
        const password = newUserForm.querySelector("#password").value.trim();

        const rolesSelected = document.getElementById("roles");

        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            roles.push({ id: option.value });
            //   if(option.value === ROLE_USER.name) {
            //      roles.push(ROLE_USER);
            // } else if (option.value === ROLE_ADMIN.name) {
            //      roles.push(ROLE_ADMIN);
            // }
        }

        const newUserData = {
            username: username,
            phoneNumber:phoneNumber,
            password: password,
            roles: roles
        };

        await createNewUser(newUserData);
        newUserForm.reset();

        document.querySelector('a#show-users-table').click();
        await fillTableOfAllUsers();
    });
}