import { ModeToggle } from '@/components/mode-toggle';
import ProductList from '@/components/product-list';
import React from 'react'

export default function Index() {
  return <div>
    <ProductList />
    <ModeToggle/>
  </div>;
}
