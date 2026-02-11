<template>
  <div class="card p-3 mb-4">
    <h3>Passer un Ordre</h3>
    <div class="form-group">
      <input v-model="order.symbol" placeholder="Symbole (ex: BTC/USD)" class="form-control mb-2">
      <input v-model.number="order.quantity" type="number" placeholder="Quantité" class="form-control mb-2">
      <input v-model.number="order.price" type="number" placeholder="Prix" class="form-control mb-2">
      <select v-model="order.side" class="form-control mb-2">
        <option value="BUY">ACHAT (BUY)</option>
        <option value="SELL">VENTE (SELL)</option>
      </select>
      <button @click="submitOrder" class="btn btn-primary w-100">Envoyer l'Ordre</button>
    </div>
  </div>
</template>

<script>
import api from '../api';

export default {
  data() {
    return {
      order: { symbol: 'BTC/USD', quantity: 1.0, price: 45000, side: 'BUY' }
    }
  },
  methods: {
    async submitOrder() {
      try {
        await api.post('/orders/send', this.order);
      } catch (error) {
        console.error("Erreur lors de l'envoi", error);
      }
    }
  }
}
</script>
<style scoped>
.card {
  background-color: #1e1e1e;
  border: 1px solid #333;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.5);
}

h3 {
  font-size: 1.2rem;
  color: #bbb;
  margin-bottom: 20px;
}

.form-control {
  background-color: #2c2c2c;
  border: 1px solid #444;
  color: white;
}

.form-control:focus {
  background-color: #333;
  color: white;
  border-color: #00ff88;
  box-shadow: none;
}

.btn-primary {
  background-color: #00ff88;
  border: none;
  color: #121212;
  font-weight: bold;
  transition: 0.3s;
}

.btn-primary:hover {
  background-color: #00cc6e;
  transform: translateY(-2px);
}
</style>