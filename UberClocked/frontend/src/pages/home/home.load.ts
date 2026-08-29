import { getProducts } from "@/services/Product";

export async function homeLoader() {
  try {
    const products = await getProducts();
    return {
      products: Array.isArray(products)
        ? products.filter(product => product && product.active && product.stock > 0)
        : []
    };
  } catch (error) {
    console.error("Error in homeLoader:", error);
    return { products: [] };
  }
}
