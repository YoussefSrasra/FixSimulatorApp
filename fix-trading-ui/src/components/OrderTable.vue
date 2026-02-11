<template>
  <div class="card p-3">
    <h3>Tableau des Ordres</h3>
    <table class="table">
      <thead>
      <tr>
        <th>ID Client</th>
        <th>Symbole</th>
        <th>Côté</th>
        <th>Quantité</th>
        <th>Prix</th>
        <th>Statut</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="o in orders" :key="o.clOrdID" :class="{ 'flash-row': o.isNew }">
        <td>{{ o.clOrdID.substring(0, 8) }}...</td>
        <td>{{ o.symbol }}</td>
        <td><span :class="o.side === 'BUY' ? 'text-success' : 'text-danger'">{{ o.side }}</span></td>
        <td>{{ o.quantity }}</td>
        <td>{{ o.price }}</td>
        <td><span class="badge" :class="statusClass(o.status)">{{ o.status }}</span></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: ['orders'],
  methods: {
    statusClass(status) {
      if (status === 'FILLED') return 'badge-success';
      if (status === 'REJECTED') return 'badge-danger';
      return 'badge-warning'; // Pour PENDING
    }
  }
}
</script>
<style scoped>
.card {
  background-color: #1e1e1e;
  border: 1px solid #333;
  border-radius: 8px;
}

.table {
  color: #e0e0e0;
}

.table thead th {
  border-top: none;
  border-bottom: 2px solid #333;
  color: #888;
  text-transform: uppercase;
  font-size: 0.8rem;
}

.table td {
  border-bottom: 1px solid #222;
  vertical-align: middle;
}

/* Badge de statut */
.badge {
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 0.75rem;
}

.badge-success { background-color: #2ecc71; color: white; }
.badge-warning { background-color: #f1c40f; color: black; }
.badge-danger { background-color: #e74c3c; color: white; }

/* Animation pour les nouvelles lignes */
.text-success { color: #00ff88 !important; font-weight: bold; }
.text-danger { color: #ff4d4d !important; font-weight: bold; }

tr:hover {
  background-color: #252525;
}

h3{
  color: white;
}

@keyframes flash-update {
  0% { background-color: rgba(0, 255, 136, 0.4); }
  100% { background-color: transparent; }
}

.flash-row {
  animation: flash-update 1.5s ease-out;
}

tr {
  transition: background-color 0.5s;
}
</style>