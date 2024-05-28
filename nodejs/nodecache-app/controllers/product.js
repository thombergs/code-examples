const productController = {
  getproducts: async (req, res) => {
    // emulating data store delay time to retrieve product data
    await new Promise(resolve => setTimeout(resolve, 750));

    const products = [
      { id: 1, name: "Desk Bed", price: 854.44 },
      { id: 2, name: "Shelf Table", price: 357.08 },
      { id: 3, name: "Couch Lamp", price: 594.53 },
      { id: 4, name: "Bed Couch", price: 309.62 },
      { id: 5, name: "Desk Shelf", price: 116.39 },
      { id: 6, name: "Couch Lamp", price: 405.03 },
      { id: 7, name: "Rug Chair", price: 47.77 },
      { id: 8, name: "Sofa Shelf", price: 359.85 },
      { id: 9, name: "Desk Table", price: 823.21 },
      { id: 10, name: "Table Shelf", price: 758.91 },
    ];

    res.json({ products });
  },
};

module.exports = { productController };
