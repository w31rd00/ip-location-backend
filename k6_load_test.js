import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 80,
  duration: '1s',
};

export default function () {
  const baseIp = '149.3.64';

  const fourthOctet = ((__VU - 1) * 10 + __ITER) % 255 + 1; // range [1,255]

  const ip = `${baseIp}.${fourthOctet}`;

  const postRes = http.post(`http://localhost:8080/ip/lookup?ip=${ip}`, null);
  check(postRes, { 'POST status is 202': (r) => r.status === 202 });

  const requestId = JSON.parse(postRes.body).requestId;

  const sseRes = http.get(`http://localhost:8080/ip/subscribe/${requestId}`);
  check(sseRes, {
    'GET status is 200/202': (r) => r.status === 200 || r.status === 202,
  });
}
