const ctx = document.querySelector('#chart').getContext('2d');

const lastReads = window.jsData.lastReads;

let labels = [];
let data = [];

for (let i = 0; i < lastReads.length; i += 1) {
  labels.push(Math.floor((lastReads[i].timestamp - lastReads[0].timestamp) / 1000));
  data.push(lastReads[i].measurement);
}


const lineChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels,
    datasets: [{
      label: 'Wartość z czujnika',
      backgroundColor: 'rgba(0, 0, 0, 0)',
      borderColor: 'rgba(255, 0, 0, 1)',
      data
    }]
  },
  options: {
    responsive: true
  }
})