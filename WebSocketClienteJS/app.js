let socket;
let myUsername = "";
let currentTarget = "all";

function connect() {
    myUsername = document.getElementById('username-input').value;
    if (!myUsername) return alert("Escribe un nombre");

    // URL del servidor local
    socket = new WebSocket("wss://uneffaced-nonregimented-karlie.ngrok-free.dev/chat");

    socket.onopen = () => {
        console.log("Conectado al servidor");

        socket.send(JSON.stringify({
            type: "CONNECT",
            from: myUsername
        }));

        document.getElementById('login-screen').classList.add('hidden');
        document.getElementById('chat-screen').classList.remove('hidden');
    };

socket.onmessage = (event) => {
        let data;
        try {
            //El servidor manda JSON
            data = JSON.parse(event.data);
            data.isMe = (data.from === myUsername);
        } catch (e) {
            //El servidor manda texto plano (ej: "Pedro: Hola")
            const rawText = event.data;
            let sender = "Desconocido";
            let messageContent = rawText;

            // Si el texto trae ": ", separamos el nombre del mensaje
            if (rawText.includes(": ")) {
                const parts = rawText.split(": ");
                sender = parts[0].replace("[Privado] ", "").trim();
                messageContent = parts.slice(1).join(": ");
            }

            data = {
                type: rawText.includes("[Privado]") ? "PRIVATE" : "BROADCAST",
                from: sender,
                content: messageContent,
                isMe: (sender === myUsername) 
            };
        }

        switch (data.type) {
            case "USER_LIST":
                updateUserList(data.users);
                break;
            case "BROADCAST":
                displayMessage(data, false);
                break;
            case "PRIVATE":
                displayMessage(data, true);
                break;
            case "SYSTEM":
                console.log("Sistema:", data.content);
                break;
            case "ERROR":
                alert("Error del servidor: " + data.content);
                break;
        }
    };

    socket.onclose = () => {
        alert("Conexión perdida");
        location.reload();
    };
}

function sendMessage() {
    const input = document.getElementById('msg-input');
    if (!input.value) return;

    const isPrivate = (currentTarget !== "all");
    let payload = {
        from: myUsername,
        content: input.value,
        type: (currentTarget === "all") ? "BROADCAST" : "PRIVATE"
    };

    if (currentTarget !== "all") {
        payload.to = currentTarget;
    }

    if (isPrivate) {
        payload.to = currentTarget;
    }

    socket.send(JSON.stringify(payload));
    if (isPrivate) {
        displayMessage({ ...payload, isMe: true }, true);
    }
    input.value = "";
}

function updateUserList(users) {
    const list = document.getElementById('user-list');
    list.innerHTML = "";

    // Botón para volver al chat global
    const divAll = document.createElement('div');
    divAll.className = `user-item ${currentTarget === 'all' ? 'active' : ''}`;
    divAll.innerText = "CHAT GLOBAL";
    divAll.onclick = () => setTarget('all');
    list.appendChild(divAll);

    users.forEach(user => {
        if (user === myUsername) return;
        const div = document.createElement('div');
        div.className = `user-item ${currentTarget === user ? 'active' : ''}`;
        div.innerText = user;
        div.onclick = () => setTarget(user);
        list.appendChild(div);
    });
}

function setTarget(user) {
    currentTarget = user;
    document.getElementById('current-target-label').innerHTML =
        `Mensaje para: <strong>${user === 'all' ? 'Todos' : user}</strong>`;

    // Actualizar CSS de la lista
    document.querySelectorAll('.user-item').forEach(el => {
        el.classList.toggle('active', el.innerText === user || (user === 'all' && el.innerText.includes("GLOBAL")));
    });
}

function displayMessage(data, isPrivate) {
    const container = document.getElementById('messages-container');
    const msgDiv = document.createElement('div');

    msgDiv.className = `msg ${data.isMe ? 'sent' : 'received'} ${isPrivate ? 'private' : ''}`;

    const sender = data.isMe ? "Tú" : data.from;
    const etiqueta = isPrivate ? " (Privado)" : "";

    msgDiv.innerHTML = `<small>${sender}${etiqueta}:</small><br>${data.content}`;

    container.appendChild(msgDiv);
    container.scrollTop = container.scrollHeight;
}

// Listeners
document.getElementById('send-btn').onclick = sendMessage;
document.getElementById('msg-input').onkeypress = (e) => { if (e.key === 'Enter') sendMessage(); };