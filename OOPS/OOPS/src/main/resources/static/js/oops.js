async function fetchJSON(url, opts={}) {
  const res = await fetch(url, { headers: { "Accept": "application/json" }, ...opts });
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
  return res.json();
}

function formatISK(amount) {
  return new Intl.NumberFormat('is-IS', { style: 'currency', currency: 'ISK', maximumFractionDigits: 0 }).format(amount);
}

async function loadMenu() {
  const status = document.getElementById('status');
  const menuEl = document.getElementById('menu');

  try {
    // Example: adjust if your API returns a different shape
    const menu = await fetchJSON('/api/menus/current'); // or '/api/menus'
    status.textContent = 'Menu';

    (menu.sections || []).sort((a,b)=>a.displayOrder-b.displayOrder).forEach(section => {
      const sec = document.createElement('div');
      sec.className = 'menu-section';
      sec.innerHTML = `<h2>${section.name}</h2>`;

      (section.items || []).forEach(item => {
        const row = document.createElement('div');
        row.className = 'item';
        row.innerHTML = `
          <div>
            <div>${item.name}</div>
            <small>${item.description || ''}</small>
          </div>
          <div class="price">${formatISK(item.price_isk ?? 0)}</div>
        `;
        sec.appendChild(row);
      });

      menuEl.appendChild(sec);
    });

  } catch (err) {
    status.textContent = 'Failed to load menu.';
    console.error(err);
  }
}

loadMenu();
