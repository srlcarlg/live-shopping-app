export const MANAGER_API_URL = 'http://127.0.0.1:8080/lives';
export const STORE_API_URL = 'http://127.0.0.1:8091/store';

export const SSE_TRANSACTIONS_URL = STORE_API_URL  + '/transactions';
export const SSE_TOTAL_PER_LIVE = STORE_API_URL + '/total'

export function CREATE_LIVE_POST(body: any) {
  return {
    url: MANAGER_API_URL,
    options: {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    },
  };
}

export function EDIT_LIVE_PASSWORD_PUT(liveSlug: string, body: any) {
  return {
    url: MANAGER_API_URL + '/' + liveSlug,
    options: {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    },
  };
}

export function ALL_LIVES_GET() {
  return {
    url: MANAGER_API_URL,
    options: {
      method: 'GET'
    },
  };
}