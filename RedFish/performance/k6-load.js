import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = (__ENV.BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
const username = __ENV.K6_USERNAME || '';
const password = __ENV.K6_PASSWORD || '';
const p95TargetMs = Number(__ENV.SLO_P95_MS || 500);
const errorRateTarget = Number(__ENV.SLO_ERROR_RATE || 0.01);

export const options = {
  stages: [
    { duration: '30s', target: 5 },
    { duration: '60s', target: 10 },
    { duration: '60s', target: 10 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: [`p(95)<${p95TargetMs}`],
    http_req_failed: [`rate<${errorRateTarget}`],
    checks: ['rate>0.99'],
  },
};

export default function () {
  const health = http.get(`${baseUrl}/actuator/health`, {
    tags: { endpoint: 'health' },
  });
  check(health, {
    'health endpoint responds 200': (r) => r.status === 200,
    'health status is UP': (r) => {
      try {
        return r.json('status') === 'UP';
      } catch (_) {
        return false;
      }
    },
  });

  if (username && password) {
    const auth = `Basic ${__ENV.K6_BASIC_AUTH || ''}`;
    const headers = __ENV.K6_BASIC_AUTH
      ? { Authorization: auth }
      : { Authorization: `Basic ${encoding.b64encode(`${username}:${password}`)}` };
    const products = http.get(`${baseUrl}/api/v1/products`, {
      headers,
      tags: { endpoint: 'products-list' },
    });
    check(products, {
      'products endpoint responds 200': (r) => r.status === 200,
      'products response is JSON': (r) => {
        try {
          return Array.isArray(r.json());
        } catch (_) {
          return false;
        }
      },
    });
  }

  sleep(1);
}
