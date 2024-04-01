// src/App.tsx
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SearchView from './components/SearchView';
import ProductDetailView from './components/ProductDetailView';
import AddProductView from "./components/AddProductView";

const App: React.FC = () => {
  return (
      <Router>
        <div>
          <Routes>
            <Route  path="/" element={<SearchView />} />
            <Route path="/product/:id" element={<ProductDetailView />} />
              <Route path="/add-product" element={<AddProductView />} />
          </Routes>
        </div>
      </Router>
  );
};

export default App;
