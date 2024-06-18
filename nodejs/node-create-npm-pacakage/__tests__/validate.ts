import { validMobileNo, validEmail, validSocial } from "../src/index";

describe("validMobileNo", () => {
  test("Valid mobile number with + sign and 15 digits", () => {
    expect(validMobileNo("+234706469321234")).toBe(true);
  });

  test("Invalid mobile number without + sign", () => {
    expect(validMobileNo("234706469321234")).toBe(false);
  });

  test("Invalid mobile number with more than 15 digits", () => {
    expect(validMobileNo("+23470646932123456")).toBe(false);
  });
});

describe("validEmail", () => {
  test("Valid email address", () => {
    expect(validEmail("example@mail.com")).toBe(true);
  });

  test('Invalid email address without "@" symbol', () => {
    expect(validEmail("examplemail.com")).toBe(false);
  });

  test("Invalid email address without domain", () => {
    expect(validEmail("example@mail")).toBe(false);
  });
});

describe("validSocial", () => {
  test("Valid Facebook URL", () => {
    expect(validSocial("https://www.facebook.com/example")).toBe(true);
  });

  test("Valid Twitter URL", () => {
    expect(validSocial("https://www.twitter.com/example")).toBe(true);
  });

  test("Invalid URL", () => {
    expect(validSocial("https://www.invalid.com/example")).toBe(false);
  });
});
