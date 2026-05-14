const API_BASE_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;

    if (path.includes('index.html') || path === '/') {
        fetchCars(renderCatalog);
        initFilters();
    } else if (path.includes('admin-dashboard.html')) {
        fetchCars(renderAdminTable);
    } else if (path.includes('car-form.html')) {
        handleFormSubmission();
    } else if (path.includes('user-bookings.html')) {
        fetchBookings(2);
    }
});

async function apiRequest(endpoint, method = 'GET', body = null) {
    try {
        const options = { method, headers: { 'Content-Type': 'application/json' } };
        if (body) options.body = JSON.stringify(body);
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        if (!response.ok) throw new Error('Network response was not ok');
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        alert('Could not reach the MobyGo backend. Is Spring Boot running on port 8080?');
    }
}

async function fetchCars(callback) {
    const cars = await apiRequest('/cars');
    if (cars) callback(cars);
}

/* ── Shared car SVG illustration (same format, tone & background for all cards) ── */
const CAR_SVG = `<svg viewBox="0 0 400 175" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Car illustration">
  <ellipse cx="200" cy="167" rx="152" ry="7" fill="rgba(0,0,0,0.22)"/>
  <path d="M38,128 L38,107 Q38,100 47,98 L92,98 L128,62 L188,46 L238,46 L282,62 L352,98 L362,98 Q370,100 370,107 L370,128 Q370,137 362,140 L47,140 Q38,137 38,128Z"
        fill="rgba(255,255,255,0.09)" stroke="rgba(255,255,255,0.2)" stroke-width="1.5"/>
  <path d="M118,98 L140,65 L188,49 L238,49 L278,65 L308,98Z"
        fill="rgba(255,255,255,0.12)" stroke="rgba(255,255,255,0.25)" stroke-width="1.5"/>
  <path d="M123,94 L144,70 L188,54 L188,94Z" fill="rgba(147,197,253,0.18)"/>
  <rect x="189" y="54" width="50" height="40" fill="rgba(147,197,253,0.16)"/>
  <path d="M240,94 L240,54 L276,70 L304,94Z" fill="rgba(147,197,253,0.18)"/>
  <line x1="215" y1="98" x2="215" y2="138" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
  <rect x="157" y="116" width="20" height="5" rx="2.5" fill="rgba(255,255,255,0.22)"/>
  <rect x="248" y="116" width="20" height="5" rx="2.5" fill="rgba(255,255,255,0.22)"/>
  <circle cx="102" cy="140" r="25" fill="#081221" stroke="rgba(255,255,255,0.15)" stroke-width="2"/>
  <circle cx="102" cy="140" r="16" fill="#0f1e30" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="102" y1="125" x2="102" y2="155" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="87"  y1="140" x2="117" y2="140" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="91"  y1="129" x2="113" y2="151" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="113" y1="129" x2="91"  y2="151" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <circle cx="102" cy="140" r="5" fill="rgba(255,255,255,0.3)"/>
  <circle cx="298" cy="140" r="25" fill="#081221" stroke="rgba(255,255,255,0.15)" stroke-width="2"/>
  <circle cx="298" cy="140" r="16" fill="#0f1e30" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="298" y1="125" x2="298" y2="155" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="283" y1="140" x2="313" y2="140" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="287" y1="129" x2="309" y2="151" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <line x1="309" y1="129" x2="287" y2="151" stroke="rgba(255,255,255,0.1)" stroke-width="1.5"/>
  <circle cx="298" cy="140" r="5" fill="rgba(255,255,255,0.3)"/>
  <path d="M356,105 L370,108 L370,119 L356,121Z" fill="rgba(253,224,71,0.82)"/>
  <path d="M44,105  L30,108  L30,119  L44,121Z"  fill="rgba(252,165,165,0.72)"/>
</svg>`;

function categoryClass(cat) {
    const c = (cat || '').toLowerCase();
    if (c === 'electric') return 'electric';
    if (c === 'hybrid')   return 'hybrid';
    if (c.includes('city')) return 'city';
    return '';
}

const PIN_ICON = `<svg width="13" height="13" viewBox="0 0 24 24" fill="none"
    stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
  <path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0 1 18 0z"/>
  <circle cx="12" cy="10" r="3"/>
</svg>`;

/* ── Catalog page ── */
function renderCatalog(cars) {
    const grid    = document.getElementById('car-grid');
    const countEl = document.getElementById('car-count');
    const statT   = document.getElementById('stat-total');
    const statA   = document.getElementById('stat-available');

    if (statT) statT.textContent = cars.length;
    if (statA) statA.textContent = cars.filter(c => c.status.toLowerCase() === 'available').length;
    if (countEl) countEl.textContent = `${cars.length} cars`;

    if (!cars.length) {
        grid.innerHTML = `<div class="empty-state" style="grid-column:1/-1">
            <div class="empty-icon">🚗</div>
            <p>No cars found in the fleet yet.</p>
        </div>`;
        return;
    }

    grid.innerHTML = cars.map(car => {
        const status    = car.status.toLowerCase();
        const available = status === 'available';
        return `
        <div class="card" data-category="${car.category}">
            <div class="card-image ${categoryClass(car.category)}">
                ${CAR_SVG}
            </div>
            <div class="card-body">
                <div class="card-header-row">
                    <div>
                        <div class="card-model">${car.model}</div>
                        <div class="card-category">${car.category}</div>
                    </div>
                    <span class="badge badge-${status}">${car.status}</span>
                </div>
                <div class="card-location">
                    ${PIN_ICON}
                    <span><strong>${car.station.name}</strong> &middot; ${car.station.city}</span>
                </div>
                <div class="card-actions">
                    <button class="btn btn-primary" ${available ? '' : 'disabled'}>
                        ${available ? 'Book Now' : car.status}
                    </button>
                    <button class="btn btn-ghost">Details</button>
                </div>
            </div>
        </div>`;
    }).join('');
}

function initFilters() {
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const filter = btn.dataset.filter;
            const cards  = document.querySelectorAll('#car-grid .card');
            let visible  = 0;

            cards.forEach(card => {
                const show = filter === 'all' || card.dataset.category === filter;
                card.style.display = show ? '' : 'none';
                if (show) visible++;
            });

            const countEl = document.getElementById('car-count');
            if (countEl) countEl.textContent = `${visible} car${visible !== 1 ? 's' : ''}`;
        });
    });
}

/* ── Admin dashboard ── */
function renderAdminTable(cars) {
    const tbody     = document.getElementById('admin-table-body');
    const statsGrid = document.getElementById('stats-grid');

    if (statsGrid) {
        const total       = cars.length;
        const available   = cars.filter(c => c.status.toLowerCase() === 'available').length;
        const rented      = cars.filter(c => ['booked', 'rented'].includes(c.status.toLowerCase())).length;
        const maintenance = cars.filter(c => c.status.toLowerCase() === 'maintenance').length;

        statsGrid.innerHTML = `
            <div class="stat-card">
                <div class="stat-label">Total Fleet</div>
                <div class="stat-value">${total}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Available</div>
                <div class="stat-value green">${available}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Booked / Rented</div>
                <div class="stat-value red">${rented}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Maintenance</div>
                <div class="stat-value slate">${maintenance}</div>
            </div>`;
    }

    tbody.innerHTML = cars.map(car => `
        <tr>
            <td style="color:var(--muted);font-size:0.8rem">#${car.id}</td>
            <td><strong>${car.licensePlate}</strong></td>
            <td>${car.model}</td>
            <td><span style="font-size:0.75rem;padding:3px 10px;background:#f1f5f9;border-radius:12px;font-weight:700">${car.category}</span></td>
            <td><span class="badge badge-${car.status.toLowerCase()}">${car.status}</span></td>
            <td style="color:var(--muted)">${car.station.name}</td>
        </tr>`).join('');
}

/* ── Car form ── */
function handleFormSubmission() {
    document.getElementById('carForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = {
            licensePlate: fd.get('licensePlate'),
            model:        fd.get('model'),
            category:     fd.get('category'),
            station:      { id: parseInt(fd.get('stationId')) }
        };
        const result = await apiRequest('/cars', 'POST', payload);
        if (result) {
            alert('Vehicle added successfully!');
            window.location.href = 'admin-dashboard.html';
        }
    });
}

/* ── Booking history ── */
async function fetchBookings(userId) {
    const bookings  = await apiRequest(`/bookings/user/${userId}`);
    const container = document.getElementById('booking-list');

    if (bookings && bookings.length > 0) {
        container.innerHTML = bookings.map(b => `
            <div class="booking-card">
                <div class="booking-left">
                    <div class="booking-id">Reservation #${b.id}</div>
                    <div class="booking-car">${b.car.model} &middot; ${b.car.category}</div>
                    <div class="booking-dates">&#128197; ${b.startDate} &rarr; ${b.endDate}</div>
                </div>
                <div class="booking-price">$${b.totalPrice}</div>
            </div>`).join('');
    } else {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">&#128663;</div>
                <p>No bookings found.<br>
                   <a href="index.html">Browse the fleet</a> to make your first reservation.</p>
            </div>`;
    }
}
