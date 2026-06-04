const API_BASE = '/api';

const Auth = {
  save(username, password) {
    sessionStorage.setItem('mg_user', username);
    sessionStorage.setItem('mg_pass', password);
    sessionStorage.setItem('mg_role', '');
  },
  clear() {
    sessionStorage.removeItem('mg_user');
    sessionStorage.removeItem('mg_pass');
    sessionStorage.removeItem('mg_role');
  },
  getUsername() { return sessionStorage.getItem('mg_user'); },
  getRole()     { return sessionStorage.getItem('mg_role'); },
  isLoggedIn()  { return !!sessionStorage.getItem('mg_user'); },
  isAdmin()     { return sessionStorage.getItem('mg_role') === 'ADMIN'; },
  header() {
    const u = sessionStorage.getItem('mg_user');
    const p = sessionStorage.getItem('mg_pass');
    if (!u || !p) return {};
    return { 'Authorization': 'Basic ' + btoa(u + ':' + p) };
  }
};

async function apiFetch(method, path, body) {
  const opts = {
    method,
    headers: { ...Auth.header(), 'Content-Type': 'application/json' }
  };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(API_BASE + path, opts);
  if (res.status === 204) return null;
  if (!res.ok) {
    const err = await res.json().catch(() => ({ message: res.statusText }));
    throw new Error(err.message || 'Request failed');
  }
  return res.json();
}

const API = {
  get:    (path)        => apiFetch('GET',    path),
  post:   (path, body)  => apiFetch('POST',   path, body),
  put:    (path, body)  => apiFetch('PUT',    path, body),
  delete: (path)        => apiFetch('DELETE', path),
};

function updateNavAuth() {
  const loginBtn  = document.getElementById('loginBtn');
  const logoutBtn = document.getElementById('logoutBtn');
  const userInfo  = document.getElementById('userInfo');
  const adminLink = document.getElementById('adminLink');

  if (Auth.isLoggedIn()) {
    loginBtn?.classList.add('d-none');
    logoutBtn?.classList.remove('d-none');
    if (userInfo) userInfo.textContent = Auth.getUsername();
    if (adminLink && Auth.isAdmin()) adminLink.classList.remove('d-none');
  } else {
    loginBtn?.classList.remove('d-none');
    logoutBtn?.classList.add('d-none');
    if (userInfo) userInfo.textContent = '';
  }
}

function logout() {
  Auth.clear();
  updateNavAuth();
  window.location.href = '/';
}

function showToast(message, type = 'success') {
  const container = document.getElementById('toastContainer');
  if (!container) return;
  const id = 'toast_' + Date.now();
  container.insertAdjacentHTML('beforeend', `
    <div id="${id}" class="toast align-items-center text-bg-${type} border-0" role="alert">
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
      </div>
    </div>`);
  const el = document.getElementById(id);
  new bootstrap.Toast(el, { delay: 3500 }).show();
  el.addEventListener('hidden.bs.toast', () => el.remove());
}
