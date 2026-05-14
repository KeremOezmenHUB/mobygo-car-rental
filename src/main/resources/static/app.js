const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;

    if (path.includes('index.html') || path === '/') {
        fetchCars(renderCatalog);
    } else if (path.includes('admin-dashboard.html')) {
        fetchCars(renderAdminTable);
    } else if (path.includes('car-form.html')) {
        handleFormSubmission();
    } else if (path.includes('user-bookings.html')) {
        fetchBookings(2); // Hardcoded user ID 2
    }
});

/** Generic Fetch Helper */
async function apiRequest(endpoint, method = 'GET', body = null) {
    try {
        const options = {
            method,
            headers: { 'Content-Type': 'application/json' }
        };
        if (body) options.body = JSON.stringify(body);

        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        if (!response.ok) throw new Error('Network response was not ok');
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to connect to MobyGo Backend. Is Spring Boot running?');
    }
}

/** 1. Catalog & Admin Logic */
async function fetchCars(callback) {
    const cars = await apiRequest('/cars');
    if (cars) callback(cars);
}

function renderCatalog(cars) {
    const container = document.getElementById('car-grid');
    container.innerHTML = cars.map(car => `
        <div class="card">
            <div class="card-content">
                <span class="badge badge-${car.status.toLowerCase()}">${car.status}</span>
                <h3 style="margin-top: 10px">${car.model}</h3>
                <p style="color: var(--text-muted)">${car.category}</p>
                <hr style="margin: 1rem 0; border: 0; border-top: 1px solid #eee">
                <p>📍 <strong>${car.station.name}</strong>, ${car.station.city}</p>
            </div>
        </div>
    `).join('');
}

function renderAdminTable(cars) {
    const tbody = document.getElementById('admin-table-body');
    tbody.innerHTML = cars.map(car => `
        <tr>
            <td>${car.id}</td>
            <td><strong>${car.licensePlate}</strong></td>
            <td>${car.model}</td>
            <td>${car.category}</td>
            <td><span class="badge badge-${car.status.toLowerCase()}">${car.status}</span></td>
            <td>${car.station.name}</td>
        </tr>
    `).join('');
}

/** 2. Form Submission Logic */
function handleFormSubmission() {
    const form = document.getElementById('carForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const payload = {
            licensePlate: formData.get('licensePlate'),
            model: formData.get('model'),
            category: formData.get('category'),
            station: { id: parseInt(formData.get('stationId')) } // Nesting per Spring standard
        };

        const result = await apiRequest('/cars', 'POST', payload);
        if (result) {
            alert('Vehicle added successfully!');
            window.location.href = 'admin-dashboard.html';
        }
    });
}

/** 3. Booking History Logic */
async function fetchBookings(userId) {
    const bookings = await apiRequest(`/bookings/user/${userId}`);
    const container = document.getElementById('booking-list');

    if (bookings && bookings.length > 0) {
        container.innerHTML = bookings.map(b => `
            <div class="card" style="margin-bottom: 1rem">
                <div class="card-content">
                    <div style="display: flex; justify-content: space-between">
                        <strong>Reservation #${b.id}</strong>
                        <span style="color: var(--accent-green); font-weight: bold">$${b.totalPrice}</span>
                    </div>
                    <p>${b.car.model}</p>
                    <small style="color: var(--text-muted)">${b.startDate} to ${b.endDate}</small>
                </div>
            </div>
        `).join('');
    } else {
        container.innerHTML = '<p>No bookings found for this user.</p>';
    }
}
