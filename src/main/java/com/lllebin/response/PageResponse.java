package com.lllebin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: Page
 * Package: com.lllebin.response
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private long total;
    private List<T> records;
}
