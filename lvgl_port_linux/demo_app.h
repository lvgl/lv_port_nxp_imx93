#ifndef DEMO_APP_H
#define DEMO_APP_H

static uint16_t window_width;
static uint16_t window_height;
static bool fullscreen;
static bool maximize;

void lv_demo_run_loop(void);
void lv_linux_disp_init(void);

#endif // DEMO_APP_H