beforeAll((done) => {
    global.provider.setup().then(() => done());
});

afterAll((done) => {
    global.provider.finalize().then(() => done());
});