package cn.qatime.player.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lungtify
 * @Time 2017/5/2 20:26
 * @Describe
 */

public class AttachmentStore {
    public AttachmentStore() {
    }

    public static long copy(String var0, String var1) {
        if (!TextUtils.isEmpty(var0) && !TextUtils.isEmpty(var1)) {
            File var2;
            if (!(var2 = new File(var0)).exists()) {
                return -1L;
            } else if (var0.equals(var1)) {
                return var2.length();
            } else {
                FileChannel var17 = null;
                FileChannel var3 = null;

                try {
                    var17 = (new FileInputStream(var2)).getChannel();
                    var3 = (new FileOutputStream(create(var1))).getChannel();
                    ByteBuffer var18 = ByteBuffer.allocateDirect(4096);

                    while (var17.read(var18) != -1) {
                        var18.flip();
                        var3.write(var18);
                        var18.clear();
                    }

                    long var4 = var2.length();
                    return var4;
                } catch (FileNotFoundException var14) {
                    var14.printStackTrace();
                } catch (IOException var15) {
                    var15.printStackTrace();
                } finally {
                    try {
                        if (var17 != null) {
                            var17.close();
                        }

                        if (var3 != null) {
                            var3.close();
                        }
                    } catch (IOException var13) {
                        var13.printStackTrace();
                    }

                }

                return -1L;
            }
        } else {
            return -1L;
        }
    }

    public static long getFileLength(String var0) {
        File var1;
        return TextUtils.isEmpty(var0) ? -1L : (!(var1 = new File(var0)).exists() ? -1L : var1.length());
    }

    public static long save(String var0, String var1) {
        return save(var1.getBytes(), var0);
    }

    public static long save(byte[] var0, String var1) {
        if (TextUtils.isEmpty(var1)) {
            return -1L;
        } else {
            File var4;
            if ((var4 = new File(var1)).getParentFile() == null) {
                return -1L;
            } else {
                if (!var4.getParentFile().exists()) {
                    var4.getParentFile().mkdirs();
                }

                try {
                    var4.createNewFile();
                    FileOutputStream var2;
                    (var2 = new FileOutputStream(var4)).write(var0);
                    var2.close();
                } catch (IOException var3) {
                    var3.printStackTrace();
                    return -1L;
                }

                return var4.length();
            }
        }
    }

    public static boolean move(String var0, String var1) {
        if (!TextUtils.isEmpty(var0) && !TextUtils.isEmpty(var1)) {
            File var2;
            if ((var2 = new File(var0)).exists() && var2.isFile()) {
                File var3;
                if ((var3 = new File(var1)).getParentFile() == null) {
                    return false;
                } else {
                    if (!var3.getParentFile().exists()) {
                        var3.getParentFile().mkdirs();
                    }

                    return var2.renameTo(var3);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static File create(String var0) {
        if (TextUtils.isEmpty(var0)) {
            return null;
        } else {
            File var2;
            if (!(var2 = new File(var0)).getParentFile().exists()) {
                var2.getParentFile().mkdirs();
            }

            try {
                var2.createNewFile();
                return var2;
            } catch (IOException var1) {
                if (var2.exists()) {
                    var2.delete();
                }

                return null;
            }
        }
    }

    public static long save(InputStream var0, String var1) {
        File var24;
        if (!(var24 = new File(var1)).getParentFile().exists()) {
            var24.getParentFile().mkdirs();
        }

        FileOutputStream var2 = null;
        boolean var15 = false;

        long var5;
        label146:
        {
            try {
                var15 = true;
                var24.createNewFile();
                var2 = new FileOutputStream(var24);
                byte[] var4 = new byte[8091];

                int var3;
                while ((var3 = var0.read(var4)) != -1) {
                    var2.write(var4, 0, var3);
                }

                var5 = var24.length();
                var15 = false;
                break label146;
            } catch (IOException var22) {
                if (var24.exists()) {
                    var24.delete();
                    var15 = false;
                } else {
                    var15 = false;
                }
            } finally {
                if (var15) {
                    try {
                        var0.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }

                    try {
                        if (var2 != null) {
                            var2.close();
                        }
                    } catch (IOException var18) {
                        var18.printStackTrace();
                    }

                }
            }

            try {
                var0.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }

            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

            return -1L;
        }

        try {
            var0.close();
        } catch (IOException var21) {
            var21.printStackTrace();
        }

        try {
            var2.close();
        } catch (IOException var20) {
            var20.printStackTrace();
        }

        return var5;
    }

    public static byte[] load(String var0) {
        try {
            File var7;
            int var1 = (int) (var7 = new File(var0)).length();
            int var2 = 0;
            byte[] var3 = new byte[var1];
            FileInputStream var8 = new FileInputStream(var7);

            int var4;
            do {
                var4 = var8.read(var3, var2, var1);
                var2 += var4;
            } while ((var1 -= var4) != 0);

            var8.close();
            return var3;
        } catch (FileNotFoundException var5) {
            return null;
        } catch (IOException var6) {
            return null;
        }
    }

    public static String loadAsString(String var0) {
        if (isFileExist(var0)) {
            byte[] var1 = load(var0);
            return new String(var1);
        } else {
            return null;
        }
    }

    public static boolean delete(String var0) {
        File var1;
        return TextUtils.isEmpty(var0) ? false : ((var1 = new File(var0)).exists() ? renameOnDelete(var1).delete() : false);
    }

    public static void deleteOnExit(String var0) {
        if (!TextUtils.isEmpty(var0)) {
            File var1;
            if ((var1 = new File(var0)).exists()) {
                var1.deleteOnExit();
            }

        }
    }

    public static boolean deleteDir(String var0) {
        return deleteDir(var0, true);
    }

    private static boolean deleteDir(String var0, boolean var1) {
        boolean var2 = true;
        File var5;
        if ((var5 = new File(var0)).exists()) {
            if (var1) {
                var5 = renameOnDelete(var5);
            }

            File[] var6;
            if ((var6 = var5.listFiles()) != null) {
                int var3 = var6.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    if (var6[var4].isDirectory()) {
                        deleteDir(var6[var4].getPath(), false);
                    } else if (!var6[var4].delete()) {
                        var2 = false;
                    }
                }
            }
        } else {
            var2 = false;
        }

        if (var2) {
            var5.delete();
        }

        return var2;
    }

    private static File renameOnDelete(File var0) {
        String var1 = var0.getParent() + "/" + System.currentTimeMillis() + "_tmp";
        File var2 = new File(var1);
        return var0.renameTo(var2) ? var2 : var0;
    }

    public static boolean isFileExist(String var0) {
        return !TextUtils.isEmpty(var0) && (new File(var0)).exists();
    }

    public static boolean saveBitmap(Bitmap var0, String var1, boolean var2) {
        if (var0 != null && !TextUtils.isEmpty(var1)) {
            BufferedOutputStream var3 = null;
            boolean var9 = false;

            label118:
            {
                try {
                    var9 = true;
                    FileOutputStream var15 = new FileOutputStream(var1);
                    var3 = new BufferedOutputStream(var15);
                    var0.compress(Bitmap.CompressFormat.JPEG, 80, var3);
                    var9 = false;
                    break label118;
                } catch (FileNotFoundException var13) {
                    var9 = false;
                } finally {
                    if (var9) {
                        if (var3 != null) {
                            try {
                                var3.close();
                            } catch (IOException var11) {
                                ;
                            }
                        }

                        if (var2) {
                            var0.recycle();
                        }

                    }
                }

                if (var3 != null) {
                    try {
                        var3.close();
                    } catch (IOException var10) {
                        ;
                    }
                }

                if (var2) {
                    var0.recycle();
                }

                return false;
            }

            try {
                var3.close();
            } catch (IOException var12) {
                ;
            }

            if (var2) {
                var0.recycle();
            }

            return true;
        } else {
            return false;
        }
    }
}
