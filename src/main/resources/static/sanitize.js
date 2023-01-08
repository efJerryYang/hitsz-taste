function sanitizeSearchQuery(query) {
    // Remove any HTML tags from the query
    console.log("Before removing HTML tags: " + query);
    query = query.replace(/<[^>]*>/g, "");
    // Remove any JavaScript event handlers from the query
    console.log("Before removing JavaScript Event Handlers: " + query);
    query = query.replace(/on\w+\s*=\s*("|')[^"']*("|')/g, "");
    // Remove any potentially malicious characters from the query
    console.log("Before removing potentially malicious characters: " + query);
    query = query.replace(/[^[\w\s-\u4e00-\u9fa5]]/g, "");
    // Trim leading and trailing whitespace
    console.log("Before trimming the query: " + query);
    query = query.trim();
    // Check if the query contains any SQL injection keywords
    const injectionKeywords = ["SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TRUNCATE", "UNION"];
    for (let keyword of injectionKeywords) {
        if (query.toUpperCase().includes(keyword)) {
            return "";
        }
    }
    // Escape special characters
    console.log("Before escaping special characters: " + query);
    query = query.replace(/'/g, "\\'");
    query = query.replace(/"/g, "\\\"");
    query = query.replace(/\\/g, "\\\\");
    // Return the sanitized query
    console.log("Processed query: " + query);
    return query;
}
