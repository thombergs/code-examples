<template>
  <div class="chart-container">
    <canvas ref="chart"></canvas>
  </div>
</template>

<script>
import Chart from "chart.js";

export default {
  name: "WeekChart",

  props: {
    chartData: {
      type: Array,
      required: true,
    },
  },

  mounted: function() {
    const config = {
      type: "bar",
      data: {
        labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
        datasets: [
          {
            data: this.chartData
          },
        ]
      },
    };

    new Chart(this.$refs.chart, config);
  }
};
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 100%;
  width: 100%;
}
</style>
