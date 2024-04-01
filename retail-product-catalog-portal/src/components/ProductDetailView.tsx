import React, { useState, useEffect } from 'react';
import {Link, useParams} from 'react-router-dom';
import { ApiResponse, Product } from '../models/models';
import './ProductDetailView.css';

const ProductDetailView: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`http://localhost:8090/api/v1/products/${id}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch data');
                }
                const data: ApiResponse = await response.json();
                if (data.status === 'SUCCESS') {
                    setProduct(data.result); // Extracting the 'result' property
                } else {
                    throw new Error(data.message || 'Unknown error');
                }
            } catch (error) {
                setError('Error fetching data');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id]);

    return (
        <div className="product-container">
            {loading && <p>Loading...</p>}
            {error && <p>{error}</p>}
            {product && (
                <div className="product-details">
                    <h2 className="product-heading">{product.name}</h2>
                    <p>Category: {product.category}</p>
                    <p>Description: {product.description}</p>
                    <p className="product-price">Price: ${product.price}</p>
                    <img src={product.imageUrl} alt={product.name} className="product-image" />
                </div>
            )}
            <div>
                <br/>
                <Link to="/" className="add-product-button">Search</Link>
            </div>
        </div>
    );
};

export default ProductDetailView;
