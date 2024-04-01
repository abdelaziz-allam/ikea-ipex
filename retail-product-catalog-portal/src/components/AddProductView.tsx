// src/pages/AddProductView.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './AddProductPage.css';

const AddProductView: React.FC = () => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        description: '',
        price: 0,
        imageUrl: '',
    });
    const [error, setError] = useState<string | null>(null);
    const history = useNavigate();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const price = parseFloat(e.target.value);
        setFormData({
            ...formData,
            price: isNaN(price) ? 0 : price,
        });
    };
    const handleCancel = () => {
        history('/'); // Redirect back to the search screen
    };
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        // Validation
        if (formData.name.trim() === '' || formData.category.trim() === '' || formData.price <= 0) {
            setError('Please fill in all required fields');
            return;
        }

        try {
            const response = await fetch('http://localhost:8090/api/v1/products', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });
            if (!response.ok) {
                throw new Error('Failed to add product');
            }
            // Redirect to the search page after adding the product
            history('/');
        } catch (error) {
            setError('Failed to add product');
        }
    };

    return (
        <div className="add-product-container">
            <h2>Add New Product</h2>
            {error && <p>{error}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Name:</label>
                    <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Category:</label>
                    <input type="text" name="category" value={formData.category} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Description:</label>
                    <textarea name="description" value={formData.description} onChange={handleInputChange} />
                </div>
                <div>
                    <label>Price:</label>
                    <input type="number" name="price" value={formData.price} onChange={handlePriceChange} required />
                </div>
                <div>
                    <label>Image URL:</label>
                    <input type="text" name="imageUrl" value={formData.imageUrl} onChange={handleInputChange} />
                </div>
                <div>
                    <button type="submit">Save</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>

                </div>
            </form>
        </div>
    );
};

export default AddProductView;
