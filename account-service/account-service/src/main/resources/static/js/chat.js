const API_BASE = '/api/v1';
let currentUser = null;

// Initialize on page load
window.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    const userStr = sessionStorage.getItem('user');
    if (!userStr) {
        window.location.href = 'index.html';
        return;
    }

    currentUser = JSON.parse(userStr);
    document.getElementById('userName').textContent = currentUser.username;

    // Show welcome message
    showWelcomeMessage();

    // Focus on input
    document.getElementById('chatInput').focus();
});

function showWelcomeMessage() {
    const chatMessages = document.getElementById('chatMessages');
    const welcomeDiv = document.createElement('div');
    welcomeDiv.className = 'welcome-message';
    welcomeDiv.innerHTML = `
        <h3>Welcome back, ${currentUser.username}!</h3>
        <p>I'm your AI banking assistant. How can I help you today?</p>
        <p style="margin-top: 10px; font-size: 13px; color: #a0aec0;">
            Try asking about your balance, recent transactions, or how to transfer money.
        </p>
    `;
    chatMessages.appendChild(welcomeDiv);
}

function logout() {
    sessionStorage.removeItem('user');
    window.location.href = 'index.html';
}

function quickAction(message) {
    document.getElementById('chatInput').value = message;
    sendMessage();
}

async function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();

    if (!message) return;

    // Clear input
    input.value = '';

    // Add user message to chat
    addMessage(message, 'user');

    // Show typing indicator
    showTypingIndicator();

    try {
        // Call AI chat API
        const response = await fetch(`${API_BASE}/ai/chat?message=${encodeURIComponent(message)}&username=${encodeURIComponent(currentUser.username)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        hideTypingIndicator();

        if (response.ok) {
            const botResponse = await response.text();
            addMessage(botResponse, 'bot');
        } else {
            addMessage('Sorry, I encountered an error. Please try again.', 'bot');
        }
    } catch (error) {
        hideTypingIndicator();
        addMessage('Sorry, I encountered a network error. Please check your connection and try again.', 'bot');
    }
}

function addMessage(text, sender) {
    const chatMessages = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;

    const avatar = sender === 'user' 
        ? `<div class="message-avatar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
           </div>`
        : `<div class="message-avatar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="12" cy="12" r="10" fill="currentColor"/>
                <path d="M8 12H16M12 8V16" stroke="white" stroke-width="2" stroke-linecap="round"/>
            </svg>
           </div>`;

    const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    messageDiv.innerHTML = `
        ${avatar}
        <div class="message-content">
            <div class="message-text">${escapeHtml(text)}</div>
            <div class="message-time">${time}</div>
        </div>
    `;

    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showTypingIndicator() {
    document.getElementById('typingIndicator').style.display = 'flex';
    const chatMessages = document.getElementById('chatMessages');
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function hideTypingIndicator() {
    document.getElementById('typingIndicator').style.display = 'none';
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Allow Enter key to send message
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && document.activeElement.id === 'chatInput') {
        sendMessage();
    }
});
