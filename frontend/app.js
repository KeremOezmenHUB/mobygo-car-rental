/* ─────────────────────────────────────────────────────────────────────────
 * MobyGo — Frontend Application
 * Works locally (localhost / file://) and in GitHub Codespaces automatically.
 * ───────────────────────────────────────────────────────────────────────── */

/** Resolve backend URL: handles localhost and GitHub Codespaces port-forwarding */
function resolveApiBase() {
    const { hostname, protocol } = window.location;
    if (hostname.includes('.app.github.dev')) {
        // Codespaces: frontend is on port 5500, backend on 8080
        return `${protocol}//${hostname.replace(/-\d+\.app\.github\.dev/, '-8080.app.github.dev')}/api`;
    }
    return 'http://localhost:8080/api';
}
const API = resolveApiBase();

/* ── Toast Notifications ─────────────────────────────────────────────────── */
function toast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.textContent = message;
    container.appendChild(t);
    setTimeout(() => {
        t.classList.add('removing');
        t.addEventListener('animationend', () => t.remove());
    }, 3500);
}

/* ── API Helper ──────────────────────────────────────────────────────────── */
async function apiRequest(endpoint, method = 'GET', body = null) {
    const options = { method, headers: { 'Content-Type': 'application/json' } };
    if (body) options.body = JSON.stringify(body);

    // NEU: Wir nutzen hier unseren neuen Datenbank-Nutzer (wird später durch echtes Login ersetzt)
    options.headers['Authorization'] = 'Basic ' + btoa('admin@mobygo.com:admin123');

    const res = await fetch(`${API}${endpoint}`, options);
    if (!res.ok) {
        const err = await res.json().catch(() => ({ error: res.statusText }));
        throw new Error(err.error || `HTTP ${res.status}`);
    }
    if (res.status === 204) return null;
    return res.json();
}

/* ── Skeleton Loaders ────────────────────────────────────────────────────── */
function renderSkeletons(containerId, count = 6) {
    const el = document.getElementById(containerId);
    if (!el) return;
    el.innerHTML = Array.from({ length: count }, () => `
        <div class="skeleton-card">
            <div class="skeleton-img"></div>
            <div class="skeleton-body">
                <div class="skeleton-line medium"></div>
                <div class="skeleton-line short"></div>
                <div class="skeleton-line short" style="margin-top:1rem"></div>
            </div>
        </div>`).join('');
}

/* ── SVG Car Illustration (identical for all cards) ─────────────────────── */
const CAR_SVG = `<svg viewBox="0 0 400 175" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
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

const PIN_ICON = `<svg width="13" height="13" viewBox="0 0 24 24" fill="none"
    stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  <path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/>
</svg>`;

function catClass(cat) {
    const c = (cat || '').toLowerCase();
    if (c === 'electric') return 'electric';
    if (c === 'hybrid')   return 'hybrid';
    if (c.includes('city')) return 'city';
    return '';
}

/* ─────────────────────────────────────────────────────────────────────────
 * PAGE ROUTING
 * ───────────────────────────────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
    const p = window.location.pathname;
    if (p.includes('index.html') || p === '/' || p.endsWith('/frontend/')) {
        initCatalogPage();
    } else if (p.includes('admin-dashboard.html')) {
        initAdminPage();
    } else if (p.includes('car-form.html')) {
        initCarForm();
    } else if (p.includes('user-bookings.html')) {
        initBookingsPage();
    } else if (p.includes('stations.html')) {
        initStationsPage();
    }
});

/* ─────────────────────────────────────────────────────────────────────────
 * CATALOG PAGE (index.html)
 * ───────────────────────────────────────────────────────────────────────── */
function initCatalogPage() {
    renderSkeletons('car-grid', 6);
    loadCars();
    initFilters();
    initBookingModal();
}

async function loadCars() {
    try {
        const cars = await apiRequest('/cars');
        renderCatalog(cars);
    } catch (e) {
        document.getElementById('car-grid').innerHTML =
            `<div class="empty-state" style="grid-column:1/-1">
                <div class="empty-icon">&#9888;</div>
                <p>Could not load fleet. Is the backend running?<br><small>${e.message}</small></p>
            </div>`;
    }
}

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
            <div class="empty-icon">&#128663;</div><p>No cars in fleet yet.</p></div>`;
        return;
    }

    grid.innerHTML = cars.map(car => {
        const status = car.status.toLowerCase();
        const avail  = status === 'available';
        return `
        <div class="card" data-category="${car.category}">
            <div class="card-image ${catClass(car.category)}">${CAR_SVG}</div>
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
                    <button class="btn btn-primary" ${avail ? `onclick="openBookingModal(${car.id},'${escHtml(car.model)}')"` : 'disabled'}>
                        ${avail ? 'Book Now' : car.status}
                    </button>
                    <button class="btn btn-ghost" onclick="showCarDetails(${car.id})">Details</button>
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
            let visible  = 0;
            document.querySelectorAll('#car-grid .card').forEach(card => {
                const show = filter === 'all' || card.dataset.category === filter;
                card.style.display = show ? '' : 'none';
                if (show) visible++;
            });
            const countEl = document.getElementById('car-count');
            if (countEl) countEl.textContent = `${visible} car${visible !== 1 ? 's' : ''}`;
        });
    });
}

function showCarDetails(id) {
    toast(`Car #${id} — details view coming soon.`, 'info');
}

/* ── Booking Modal ── */
function initBookingModal() {
    const today = new Date().toISOString().split('T')[0];
    const startEl = document.getElementById('modal-start');
    const endEl   = document.getElementById('modal-end');
    if (startEl) startEl.min = today;

    [startEl, endEl].forEach(el => {
        if (el) el.addEventListener('change', updatePricePreview);
    });

    // Close on backdrop click
    const backdrop = document.getElementById('booking-modal');
    if (backdrop) backdrop.addEventListener('click', e => { if (e.target === backdrop) closeModal(); });

    // Close on Escape
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModal(); });
}

function openBookingModal(carId, carModel) {
    document.getElementById('modal-car-id').value  = carId;
    document.getElementById('modal-car-name').textContent = carModel;
    document.getElementById('booking-error').style.display = 'none';
    document.getElementById('price-preview').style.display = 'none';
    document.getElementById('modal-start').value = '';
    document.getElementById('modal-end').value   = '';
    document.getElementById('booking-modal').classList.add('open');
}

function closeModal() {
    document.getElementById('booking-modal').classList.remove('open');
}

function updatePricePreview() {
    const s = document.getElementById('modal-start').value;
    const e = document.getElementById('modal-end').value;
    const preview = document.getElementById('price-preview');
    if (s && e && e > s) {
        const days = Math.round((new Date(e) - new Date(s)) / 86400000);
        document.getElementById('price-amount').textContent = `CHF ${(days * 50).toFixed(2)}`;
        preview.style.display = 'flex';
    } else {
        preview.style.display = 'none';
    }
}

async function submitBooking() {
    const errEl = document.getElementById('booking-error');
    errEl.style.display = 'none';

    const payload = {
        car:       { id: parseInt(document.getElementById('modal-car-id').value) },
        user:      { id: parseInt(document.getElementById('modal-user').value) },
        startDate: document.getElementById('modal-start').value,
        endDate:   document.getElementById('modal-end').value
    };

    if (!payload.startDate || !payload.endDate) {
        errEl.textContent = 'Please select start and end dates.';
        errEl.style.display = 'block';
        return;
    }
    if (payload.startDate >= payload.endDate) {
        errEl.textContent = 'End date must be after start date.';
        errEl.style.display = 'block';
        return;
    }

    try {
        await apiRequest('/bookings', 'POST', payload);
        closeModal();
        toast('Booking confirmed! &#10003;', 'success');
        loadCars(); // Refresh fleet to update status
    } catch (e) {
        errEl.textContent = e.message;
        errEl.style.display = 'block';
    }
}

/* ─────────────────────────────────────────────────────────────────────────
 * ADMIN PAGE (admin-dashboard.html)
 * ───────────────────────────────────────────────────────────────────────── */
function initAdminPage() {
    loadAdminCars();

    // Close edit modal on backdrop click / Escape
    const backdrop = document.getElementById('edit-modal');
    if (backdrop) backdrop.addEventListener('click', e => { if (e.target === backdrop) closeEditModal(); });
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closeEditModal(); });
}

async function loadAdminCars() {
    try {
        const cars = await apiRequest('/cars');
        renderStats(cars);
        renderAdminTable(cars);
    } catch (e) {
        toast('Failed to load fleet: ' + e.message, 'error');
    }
}

function renderStats(cars) {
    const grid = document.getElementById('stats-grid');
    if (!grid) return;
    const total   = cars.length;
    const avail   = cars.filter(c => c.status.toLowerCase() === 'available').length;
    const rented  = cars.filter(c => ['booked', 'rented'].includes(c.status.toLowerCase())).length;
    const maint   = cars.filter(c => c.status.toLowerCase() === 'maintenance').length;

    grid.innerHTML = `
        <div class="stat-card"><div class="stat-label">Total Fleet</div><div class="stat-value">${total}</div></div>
        <div class="stat-card"><div class="stat-label">Available</div><div class="stat-value green">${avail}</div></div>
        <div class="stat-card"><div class="stat-label">Booked / Rented</div><div class="stat-value red">${rented}</div></div>
        <div class="stat-card"><div class="stat-label">Maintenance</div><div class="stat-value slate">${maint}</div></div>`;
}

function renderAdminTable(cars) {
    document.getElementById('admin-table-body').innerHTML = cars.map(car => `
        <tr>
            <td style="color:var(--muted);font-size:0.8rem">#${car.id}</td>
            <td><strong>${car.licensePlate}</strong></td>
            <td>${car.model}</td>
            <td><span style="font-size:0.75rem;padding:3px 10px;background:#f1f5f9;border-radius:12px;font-weight:700">${car.category}</span></td>
            <td><span class="badge badge-${car.status.toLowerCase()}">${car.status}</span></td>
            <td style="color:var(--muted)">${car.station.name}</td>
            <td>
                <div style="display:flex;gap:6px">
                    <button class="btn btn-ghost" style="padding:6px 12px;font-size:0.78rem"
                        onclick="openEditModal(${car.id},'${escHtml(car.licensePlate)}','${escHtml(car.model)}','${car.category}','${car.status}',${car.station.id})">
                        &#9998; Edit
                    </button>
                    <button class="btn btn-danger" style="padding:6px 12px;font-size:0.78rem"
                        onclick="deleteCar(${car.id},'${escHtml(car.model)}')">
                        &#128465; Delete
                    </button>
                </div>
            </td>
        </tr>`).join('');
}

/* ── Edit Modal ── */
function openEditModal(id, plate, model, category, status, stationId) {
    document.getElementById('edit-car-id').value  = id;
    document.getElementById('edit-plate').value   = plate;
    document.getElementById('edit-model').value   = model;
    document.getElementById('edit-category').value = category;
    document.getElementById('edit-status').value  = status;
    document.getElementById('edit-station').value = stationId;
    document.getElementById('edit-error').style.display = 'none';
    document.getElementById('edit-modal').classList.add('open');
}

function closeEditModal() {
    document.getElementById('edit-modal').classList.remove('open');
}

async function submitEdit() {
    const errEl = document.getElementById('edit-error');
    errEl.style.display = 'none';
    const id = document.getElementById('edit-car-id').value;
    const payload = {
        licensePlate: document.getElementById('edit-plate').value,
        model:        document.getElementById('edit-model').value,
        category:     document.getElementById('edit-category').value,
        status:       document.getElementById('edit-status').value,
        station:      { id: parseInt(document.getElementById('edit-station').value) }
    };
    try {
        await apiRequest(`/cars/${id}`, 'PUT', payload);
        closeEditModal();
        toast('Vehicle updated successfully.', 'success');
        loadAdminCars();
    } catch (e) {
        errEl.textContent = e.message;
        errEl.style.display = 'block';
    }
}

async function deleteCar(id, model) {
    if (!confirm(`Delete "${model}" (#${id})? This cannot be undone.`)) return;
    try {
        await apiRequest(`/cars/${id}`, 'DELETE');
        toast(`"${model}" deleted.`, 'success');
        loadAdminCars();
    } catch (e) {
        toast('Delete failed: ' + e.message, 'error');
    }
}

/* ─────────────────────────────────────────────────────────────────────────
 * CAR FORM PAGE (car-form.html)
 * ───────────────────────────────────────────────────────────────────────── */
function initCarForm() {
    document.getElementById('carForm').addEventListener('submit', async e => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const payload = {
            licensePlate: fd.get('licensePlate'),
            model:        fd.get('model'),
            category:     fd.get('category'),
            station:      { id: parseInt(fd.get('stationId')) }
        };
        try {
            await apiRequest('/cars', 'POST', payload);
            toast('Vehicle added successfully!', 'success');
            setTimeout(() => { window.location.href = 'admin-dashboard.html'; }, 1200);
        } catch (e) {
            toast('Error: ' + e.message, 'error');
        }
    });
}

/* ─────────────────────────────────────────────────────────────────────────
 * BOOKINGS PAGE (user-bookings.html)
 * ───────────────────────────────────────────────────────────────────────── */
async function initBookingsPage() {
    const container = document.getElementById('booking-list');
    container.innerHTML = `<div class="skeleton-card" style="border-radius:8px;margin-bottom:1rem">
        <div class="skeleton-body"><div class="skeleton-line medium"></div><div class="skeleton-line short"></div></div>
    </div>`.repeat(3);
    try {
        const bookings = await apiRequest('/bookings/user/2');
        if (bookings && bookings.length > 0) {
            container.innerHTML = bookings.map(b => `
                <div class="booking-card">
                    <div class="booking-left">
                        <div class="booking-id">Reservation #${b.id}</div>
                        <div class="booking-car">${b.car.model} &middot; ${b.car.category}</div>
                        <div class="booking-dates">&#128197; ${b.startDate} &rarr; ${b.endDate}</div>
                    </div>
                    <div class="booking-price">CHF ${b.totalPrice.toFixed(2)}</div>
                </div>`).join('');
        } else {
            container.innerHTML = `
                <div class="empty-state">
                    <div class="empty-icon">&#128663;</div>
                    <p>No bookings yet.<br><a href="index.html">Browse the fleet</a> to make your first reservation.</p>
                </div>`;
        }
    } catch (e) {
        container.innerHTML = `<div class="empty-state"><div class="empty-icon">&#9888;</div>
            <p>Could not load bookings.<br><small>${e.message}</small></p></div>`;
    }
}

/* ─────────────────────────────────────────────────────────────────────────
 * STATIONS PAGE (stations.html)
 * ───────────────────────────────────────────────────────────────────────── */
async function initStationsPage() {
    renderSkeletons('station-grid', 4);
    try {
        const [stations, cars] = await Promise.all([
            apiRequest('/stations'),
            apiRequest('/cars')
        ]);

        const countEl = document.getElementById('station-count');
        if (countEl) countEl.textContent = `${stations.length} locations`;

        const grid = document.getElementById('station-grid');
        grid.innerHTML = stations.map(st => {
            const stCars  = cars.filter(c => c.station.id === st.id);
            const avail   = stCars.filter(c => c.status.toLowerCase() === 'available').length;
            return `
            <div class="station-card">
                <div class="station-name">${st.name}</div>
                <div class="station-city">${st.city}</div>
                <div class="station-address">${PIN_ICON} ${st.address}</div>
                <div class="station-stats">
                    <div class="station-stat-item"><div class="val">${stCars.length}</div><div class="lbl">Total</div></div>
                    <div class="station-stat-item"><div class="val" style="color:#15803d">${avail}</div><div class="lbl">Available</div></div>
                    <div class="station-stat-item"><div class="val" style="color:#dc2626">${stCars.length - avail}</div><div class="lbl">Unavailable</div></div>
                </div>
            </div>`;
        }).join('');
    } catch (e) {
        document.getElementById('station-grid').innerHTML =
            `<div class="empty-state"><div class="empty-icon">&#9888;</div><p>${e.message}</p></div>`;
    }
}

/* ─────────────────────────────────────────────────────────────────────────
 * UTILS
 * ───────────────────────────────────────────────────────────────────────── */
function escHtml(str) {
    return String(str).replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
// Modal öffnen
function openRegisterModal() {
    document.getElementById('register-modal').classList.add('open');
}

// Modal schliessen
function closeRegisterModal() {
    document.getElementById('register-modal').classList.remove('open');
}

// Registrierung an das Backend senden
async function submitRegistration() {
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const errEl = document.getElementById('reg-error');

    try {
        const res = await fetch(`${API}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) throw new Error(await res.text());

        toast('Registrierung erfolgreich! Bitte logge dich ein.');
        closeRegisterModal();
    } catch (e) {
        errEl.textContent = e.message;
        errEl.style.display = 'block';
    }
}