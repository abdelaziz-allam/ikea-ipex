
export interface ApiResponse {
    status: string;
    result: Product;
    code: string;
    message: string | null;
}

export interface Product {
    id: string;
    name: string;
    category: string;
    description: string;
    price: number;
    imageUrl: string;
}
