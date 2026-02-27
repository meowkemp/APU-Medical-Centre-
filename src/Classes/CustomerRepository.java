package Classes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CustomerRepository {

    private static final java.util.regex.Pattern CUST_ID_RX =
            java.util.regex.Pattern.compile("^C(\\d{4})$");

    private final Path file;

    public CustomerRepository(String filename) {
        this.file = Paths.get(filename);
        ensureFileExists();
    }

    /** Return only CUSTOMER rows from users.txt */
    public List<Customer> readAll() {
        List<Customer> list = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 2) continue;
                if (!"CUSTOMER".equalsIgnoreCase(p[1])) continue;

                String id       = get(p, 0);
                String name     = get(p, 2);
                String email    = get(p, 3);
                String password = get(p, 4);
                String phone    = get(p, 5);
                String ic       = get(p, 8);
                String address  = get(p, 9);

                Customer c = new Customer(id, name, email, phone, ic, password, address);
                c.setActive(true); // no 'active' column in users.txt
                list.add(c);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return list;
    }

    public Customer findById(String id) {
        return readAll().stream()
                .filter(c -> id.equalsIgnoreCase(c.getId()))
                .findFirst()
                .orElse(null);
    }

    /** Create new (append/merge into users.txt) */
    public void create(Customer c) {
        if (c.getId() == null || c.getId().isBlank()) {
            c.setId(nextId());
        }
        List<Customer> all = readAll();
        all.add(c);
        writeAll(all);
    }

    public void save(Customer c, boolean allowCreate) {
        List<Customer> all = readAll();
        int idx = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equalsIgnoreCase(c.getId())) {
                idx = i; break;
            }
        }
        if (idx >= 0) {
            all.set(idx, c);
        } else if (allowCreate) {
            all.add(c);
        } else {
            throw new NoSuchElementException("No customer with ID " + c.getId());
        }
        writeAll(all);
    }

    public boolean delete(String id, boolean hardDelete) {
        List<Customer> all = readAll();
        boolean removed = all.removeIf(c -> c.getId().equalsIgnoreCase(id));
        if (removed) writeAll(all);
        return removed;
    }

    /** Show the next C####. */
    public String peekNextCustomerId() { return nextId(); }

    /* ===== helpers ===== */

    private void writeAll(List<Customer> customers) {
        try {
            // keep non-customer lines
            List<String> original = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<String> kept = new ArrayList<>();
            for (String line : original) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length >= 2 && "CUSTOMER".equalsIgnoreCase(p[1])) {
                    // skip old customer rows; we'll rewrite from 'customers'
                    continue;
                }
                kept.add(line);
            }
            // append customers, sorted by id
            customers.sort(Comparator.comparing(Customer::getId));
            for (Customer c : customers) {
                kept.add(toUsersRow(c));
            }
            Files.write(file, kept, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String toUsersRow(Customer c) {
        return String.join("|",
                nz(c.getId()),
                "CUSTOMER",
                nz(c.getName()),
                nz(c.getEmail()),
                nz(c.getPassword()),
                nz(c.getPhone()),
                "", "",                                // placeholders
                nz(c.getCustomerIc()),
                nz(c.getAddress())
        );
    }

    private String nextId() {
        int max = 0;
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length >= 1 && p[0].startsWith("C")) {
                    try {
                        max = Math.max(max, Integer.parseInt(p[0].substring(1)));
                    } catch (NumberFormatException ignore) {}
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return String.format("C%04d", max + 1);
    }

    private void ensureFileExists() {
        try { if (!Files.exists(file)) Files.createDirectories(file.getParent()); } catch (IOException ignore) {}
        try { if (!Files.exists(file)) Files.createFile(file); } catch (IOException ignore) {}
    }

    private static String get(String[] a, int i) { return i < a.length ? a[i] : ""; }
    private static String nz(String s) { return s == null ? "" : s.replace("|", "/"); }
}
