import { shallowMount } from "@vue/test-utils";
import WeekChart from "../../components/WeekChart.vue";

describe("WeekChart", () => {
  it("renders without error", () => {
    const wrapper = shallowMount(WeekChart, {
      propsData: {
        chartData: [1, 2, 3, 4, 5, 6, 7],
      },
    });

    const chart = wrapper.findComponent({ name: "WeekChart" });
    expect(chart.exists()).toBe(true);
  });

});
