function sanitizeSearchQuery(query) {
    // Remove any HTML tags from the query
    query = query.replace(/<[^>]*>/g, "");
    // Remove any JavaScript event handlers from the query
    query = query.replace(/on\w+\s*=\s*("|')[^"']*("|')/g, "");
    // Remove any potentially malicious characters from the query
    query = query.replace(/[^\w\s-]/g, "");
    // Trim leading and trailing whitespace
    query = query.trim();
    // Check if the query contains any SQL injection keywords
    const injectionKeywords = ["SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TRUNCATE", "UNION"];
    for (let keyword of injectionKeywords) {
        if (query.toUpperCase().includes(keyword)) {
            return "";
        }
    }
    // Escape special characters
    query = query.replace(/'/g, "\\'");
    query = query.replace(/"/g, "\\\"");
    query = query.replace(/\\/g, "\\\\");
    // Return the sanitized query
    return query;
}
