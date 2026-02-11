<template>
  <div id="app" class="container mt-5">
    <h1 class="text-center mb-4">FIX Simulator Dashboard</h1>
    <div class="row">
      <div class="col-md-4">
        <OrderForm />
      </div>
      <div class="col-md-8">
        <OrderTable :orders="orders" />
      </div>
    </div>
  </div>
</template>

<script>
import OrderForm from './components/OrderForm.vue';
import OrderTable from './components/OrderTable.vue';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import api from './api';

export default {
  components: { OrderForm, OrderTable },
  data() {
    return {
      orders: [],
      stompClient: null
    }
  },
  async mounted() {
    try {
      const response = await api.get('/orders');
      this.orders = Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      console.error("Erreur chargement ordres:", error);
      this.orders = [];
    }
    this.connectWebSocket();
  },
  methods: {
    connectWebSocket() {
      const socket = new SockJS('http://localhost:8080/ws-trading');
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, () => {
        console.log("Connecté au WebSocket !");

        this.stompClient.subscribe('/topic/orders', (message) => {
          const updatedOrder = JSON.parse(message.body);
          this.updateOrderList(updatedOrder);
        });
      });
    },
    updateOrderList(updatedOrder) {
      updatedOrder.isNew = true;
      if (!Array.isArray(this.orders)) {
        this.orders = [];
      }
      const index = this.orders.findIndex(o => o.clOrdID === updatedOrder.clOrdID);
      if (index !== -1) {
        if (this.orders[index].status !== 'FILLED' && updatedOrder.status === 'FILLED') {
          this.$toast.success(`Exécuté : ${updatedOrder.side} ${updatedOrder.quantity} ${updatedOrder.symbol} @ ${updatedOrder.price}`);
        }
        this.$set(this.orders, index, updatedOrder);
      } else {
        this.$toast.info(`Nouvel ordre envoyé : ${updatedOrder.symbol}`);
        this.orders.unshift(updatedOrder);
      }
      setTimeout(() => {
        updatedOrder.isNew = false;
      }, 1500);
    }
  }
}
</script>


<style>
body {
  background-color: #121212; /* Fond sombre */
  color: #e0e0e0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.container {
  max-width: 1200px;
}

h1 {
  color: #00ff88; /* Vert fluo style terminal */
  text-transform: uppercase;
  font-weight: bold;
  letter-spacing: 2px;
  border-bottom: 2px solid #333;
  padding-bottom: 10px;
}
</style>