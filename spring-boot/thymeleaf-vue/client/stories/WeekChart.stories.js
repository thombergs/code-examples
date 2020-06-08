import WeekChart from '../src/components/WeekChart.vue';

export default {
  title: 'WeekChart',
  component: WeekChart,
};

export const DefaultState = () => ({
  components: { 
    chart: WeekChart
  },
  template: `<chart v-bind:chartData="[1,2,3,4,5,6,7]" />`
});