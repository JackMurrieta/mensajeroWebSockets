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
        
        // ENVÍO DE CONNECT
        socket.send(JSON.stringify({
            type: "CONNECT",
            from: myUsername
        }));

        document.getElementById('login-screen').classList.add('hidden');
        document.getElementById('chat-screen').classList.remove('hidden');
    };

    socket.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log("Recibido:", data);

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

    let payload = {
        from: myUsername,
        content: input.value,
        type: (currentTarget === "all") ? "BROADCAST" : "PRIVATE"
    };

    if (currentTarget !== "all") {
        payload.to = currentTarget;
    }

    socket.send(JSON.stringify(payload));
    displayMessage({...payload, isMe: true}, currentTarget !== "all");
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
document.getElementById('msg-input').onkeypress = (e) => { if(e.key === 'Enter') sendMessage(); };