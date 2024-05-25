/**
 * Validates a mobile number, ensuring it starts with a "+" sign
 * and contains only digits, with a maximum length of 15 characters.
 * @param {string} mobileNumber
 * @returns {boolean}
 * @example
 * validateMobileNumber("+23470646932") // Output: true
 */
export const validMobileNo = (mobileNumber: string): boolean => {
  if (mobileNumber.charAt(0) === "+") {
    const numberWithoutPlus = mobileNumber.slice(1);

    if (!isNaN(Number(numberWithoutPlus)))
      return numberWithoutPlus.length <= 15;
  }
  return false;
};

/**
 * Validates an email address using a regular expression.
 * @param {string} email
 * @returns {boolean}
 * @example
 * validateEmail("example@mail.com") // Output: true
 */
export const validEmail = (email: string): boolean => {
  const emailRegex: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * Validates a social media URL for Facebook or Twitter.
 * @param {string} url
 * @returns {boolean}
 * @example
 * validateSocialURL("https://www.facebook.com/example") // Output: true
 * validateSocialURL("https://www.twitter.com/example") // Output: true
 */
export const validSocial = (url: string): boolean => {
  const socialRegexMap: Map<string, RegExp> = new Map([
    ["facebook", /^(https?:\/\/)?(www\.)?facebook.com\/[a-zA-Z0-9._-]+\/?$/],
    ["twitter", /^(https?:\/\/)?(www\.)?twitter.com\/[a-zA-Z0-9_]+\/?$/],
    // Add more social platforms' regex patterns here
  ]);
  return Array.from(socialRegexMap.values()).some(regex => regex.test(url));
};
