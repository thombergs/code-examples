import axios from 'axios';

type Product = {
  id: number;
  email: string;
  first_name: string;
};

type GetProductsResponse = {
  data: Product[];
};

async function getProducts() {
  try {
    // 👇️ fetch products from an API
    const { data, status } = await axios.get<GetProductsResponse>(
      'http://localhost:3002/products',
      {
        headers: {
          Accept: 'application/json',
        },
      },
    );

    console.log(JSON.stringify(data, null, 4));

    console.log(`response status is: ${status}`);

    return data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.log(`error message: ${error.message}`);
      return error.message;
    } else {
      console.log(`unexpected error: ${error}`);
      return 'An unexpected error occurred';
    }
  }
}

getProducts();
