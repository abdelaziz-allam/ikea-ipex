import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Product } from '../models/models';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@mui/material';
import './SearchView.css';

const SearchView: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState<Product[]>([]);
    const [loading, setLoading] = useState(false); // For loading state
    const [error, setError] = useState<string | null>(null); // For error handling
    const [tableData, setTableData] = useState<Product[]>([]);
    const [debouncedSearchTerm, setDebouncedSearchTerm] = useState<string>('');

    // Debounce function to delay the API call
    const debounce = (func: Function, delay: number) => {
        let timer: NodeJS.Timeout; // Provide type annotation for timer

        return (...args: any[]) => {
            clearTimeout(timer);
            timer = setTimeout(() => func(...args), delay) as NodeJS.Timeout; // Adjust clearTimeout usage
        };
    };


    useEffect(() => {
        const fetchTableData = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`http://localhost:8090/api/v1/products?offset=0&pageSize=100`);
                if (!response.ok) {
                    throw new Error('Failed to fetch table data');
                }
                const data = await response.json();
                const results: Product[] = data.result;
                setTableData(results);
            } catch (error) {
                setError('Error fetching table data');
            } finally {
                setLoading(false);
            }
        };

        fetchTableData();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`http://localhost:8090/api/v1/products/search?term=${debouncedSearchTerm}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch search data');
                }
                const data = await response.json();
                const results: Product[] = data.result;
                setSearchResults(results);
            } catch (error) {
                setError('Error fetching search data');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [debouncedSearchTerm]);

    useEffect(() => {
        // Debounce the search term input
        const debouncedHandler = debounce(() => {
            setDebouncedSearchTerm(searchTerm);
        }, 500); // Adjust the delay as needed

        debouncedHandler();

        // Cleanup function to clear timeout
        return () => {
            debouncedHandler(); // Call the debounced function to clear the timer
        };
    }, [searchTerm]);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
    };

    const handleAddProductClick = () => {
        window.location.href = '/add-product';
    };

    return (
        <div className="search-container">
            <input type="text" value={searchTerm} onChange={handleSearchChange} className="search-input" placeholder="Search products..." />
            {loading && <p>Loading...</p>}
            {error && <p>{error}</p>}

            <Link to="/add-product" className="add-product-button" onClick={(e) => {
                e.preventDefault();
                handleAddProductClick();
            }}>Add Product</Link>

            <TableContainer component={Paper} className="table-container">
                <Table sx={{ minWidth: 650 }} aria-label="product table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Category</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell>Action</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {searchResults.map((product) => (
                            <TableRow key={product.id}>
                                <TableCell>{product.name}</TableCell>
                                <TableCell>{product.category}</TableCell>
                                <TableCell>${product.price}</TableCell>
                                <TableCell>
                                    <Link to={`/product/${product.id}`} className="action-link">View Details</Link>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default SearchView;
